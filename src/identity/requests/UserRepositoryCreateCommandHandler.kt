package cozy.identity.requests

import com.trendyol.kediatr.AsyncCommandHandler
import cozy.exception.middleware.StatusException
import cozy.identity.data.ClientAuthSigningRequest
import cozy.identity.extensions.toPemString
import cozy.identity.repositories.SigningRequestRepository
import cozy.identity.services.CertificateService
import cozy.identity.services.SigningRequestService
import io.fabric8.kubernetes.client.KubernetesClientException
import io.ktor.http.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class UserRepositoryCreateCommandHandler: AsyncCommandHandler<UserRepositoryCreateCommand>, KoinComponent {

    private val signingRequestRepository: SigningRequestRepository by inject()

    private val certificateService: CertificateService by inject()
    private val signingRequestService: SigningRequestService by inject()

    override suspend fun handleAsync(command: UserRepositoryCreateCommand) {
        val keyPair = certificateService.generateKeyPair()
        val clientKey = keyPair.private.toPemString()

        val signingRequestData = certificateService.buildSigningRequest(keyPair, command.userIdentity.toPrincipal())
        val authSigningRequest = ClientAuthSigningRequest(command.userIdentity.id, clientKey, signingRequestData)

        try {
            val signingRequest = signingRequestRepository.create(authSigningRequest.certificateSigningRequest)
            signingRequestService.approve(signingRequest)
        } catch (e: StatusException) {
            if (e.cause is KubernetesClientException)
                throw StatusException(HttpStatusCode.BadRequest, "User '${command.userIdentity.id}' already exists.")

            signingRequestRepository.delete(authSigningRequest.certificateSigningRequest)

            throw StatusException(
                HttpStatusCode.InternalServerError,
                "User '${command.userIdentity.id}' could not be approved."
            )
        }
    }

}