package cozy.repositories.user

import cozy.middleware.exception.StatusException
import cozy.repositories.cert.SigningRequestRepository
import cozy.repositories.cert.data.ClientAuthSigningRequest
import cozy.services.cert.SigningRequestService
import cozy.services.cluster.data.ClusterUser
import cozy.services.cert.CertificateService
import cozy.services.cert.toPemString
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class UserRepositoryImpl : UserRepository, KoinComponent {

    private val signingRequestRepository: SigningRequestRepository by inject()

    private val certificateService: CertificateService by inject()
    private val signingRequestService: SigningRequestService by inject()

    override suspend fun retrieve(userIdentity: ClusterUser): ClusterUser = coroutineScope {
        val signingRequest = signingRequestRepository.retrieve(userIdentity.id)
            ?: throw StatusException(HttpStatusCode.NotFound, "Signing Request for '$userIdentity.id' does not exists.")

        if (signingRequest.clientCert == null)
            throw StatusException(HttpStatusCode.BadRequest, "Signing Request has been denied or is still pending.")

        ClusterUser(signingRequest.clientCert!!, signingRequest.clientKey)
    }

    override suspend fun create(userIdentity: ClusterUser): ClusterUser = coroutineScope {
        val keyPair = certificateService.generateKeyPair()
        val clientKey = keyPair.private.toPemString()
        
        val signingRequestData = certificateService.buildSigningRequest(keyPair, userIdentity.toPrincipal())
        val authSigningRequest = ClientAuthSigningRequest(userIdentity.id, clientKey, signingRequestData)

        val signingRequest = signingRequestRepository.create(authSigningRequest.kubernetes)
        signingRequestService.approve(signingRequest)

        retrieve(userIdentity)
    }

    override suspend fun delete(userIdentity: ClusterUser): Boolean = coroutineScope {
        val signingRequest = signingRequestRepository.retrieve(userIdentity.id)
            ?: throw StatusException(HttpStatusCode.NotFound, "Signing Request for '${userIdentity.id}' does not exists.")

        signingRequestRepository.delete(signingRequest.certificateSigningRequest)
    }
}