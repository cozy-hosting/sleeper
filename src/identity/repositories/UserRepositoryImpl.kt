package cozy.auth.repositories

import cozy.auth.repositories.data.ClientAuthSigningRequest
import cozy.identity.services.SigningRequestService
import cozy.services.cluster.data.ClusterUser
import cozy.identity.services.CertificateService
import cozy.auth.services.toPemString
import cozy.identity.repositories.SigningRequestRepository
import cozy.identity.repositories.UserRepository
import io.fabric8.kubernetes.client.KubernetesClientException
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.RuntimeException
import javax.naming.OperationNotSupportedException

@KoinApiExtension
class UserRepositoryImpl : UserRepository, KoinComponent {

    private val signingRequestRepository: SigningRequestRepository by inject()

    private val certificateService: CertificateService by inject()
    private val signingRequestService: SigningRequestService by inject()

    override suspend fun retrieve(userIdentity: ClusterUser): ClusterUser = coroutineScope {
        val signingRequest = signingRequestRepository.retrieve(userIdentity.id)
            ?: throw OperationNotSupportedException("Signing Request for '${userIdentity.id}' does not exists.")

        if (signingRequest.clientCert == null)
            throw OperationNotSupportedException("Signing Request has been denied, or is still pending.")

        ClusterUser(signingRequest.clientCert!!, signingRequest.clientKey)
    }

    override suspend fun create(userIdentity: ClusterUser): ClusterUser = coroutineScope {
        val keyPair = certificateService.generateKeyPair()
        val clientKey = keyPair.private.toPemString()
        
        val signingRequestData = certificateService.buildSigningRequest(keyPair, userIdentity.toPrincipal())
        val authSigningRequest = ClientAuthSigningRequest(userIdentity.id, clientKey, signingRequestData)

        try {
            val signingRequest = signingRequestRepository.create(authSigningRequest.certificateSigningRequest)
            signingRequestService.approve(signingRequest)

            retrieve(userIdentity)
        } catch (e: KubernetesClientException) {
            throw OperationNotSupportedException("User '${userIdentity.id}' already exists.")
        } catch (e: RuntimeException) {
            signingRequestRepository.delete(authSigningRequest.certificateSigningRequest)

            throw OperationNotSupportedException("User '${userIdentity.id}' could not be approved.")
        }
    }

    override suspend fun delete(userIdentity: ClusterUser): Boolean = coroutineScope {
        val signingRequest = signingRequestRepository.retrieve(userIdentity.id)
            ?: throw OperationNotSupportedException("Signing Request for '${userIdentity.id}' does not exists.")

        signingRequestRepository.delete(signingRequest.certificateSigningRequest)
    }
}