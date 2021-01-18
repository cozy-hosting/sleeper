package cozy.identity.requests

import com.trendyol.kediatr.AsyncCommandHandler
import cozy.cluster.services.ServiceClusterClient
import cozy.exception.middleware.StatusException
import io.fabric8.kubernetes.client.KubernetesClientException
import io.ktor.http.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class SigningRequestRepositoryCreateCommandHandler : AsyncCommandHandler<SigningRequestRepositoryCreateCommand>,
    KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun handleAsync(command: SigningRequestRepositoryCreateCommand) {
        try {
            clusterClient.connectAsService {
                certificateSigningRequests().create(command.certificateSigningRequest)
            }
        } catch (e: KubernetesClientException) {
            throw StatusException(
                HttpStatusCode.BadRequest,
                "Certificate signing request '${command.certificateSigningRequest.metadata.name}' already exists.",
                e
            )
        }
    }

}