package cozy.identity.requests

import com.trendyol.kediatr.AsyncCommandHandler
import cozy.cluster.services.ServiceClusterClient
import cozy.exception.middleware.StatusException
import io.ktor.http.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class SigningRequestRepositoryDeleteCommandHandler : AsyncCommandHandler<SigningRequestRepositoryDeleteCommand>,
    KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun handleAsync(command: SigningRequestRepositoryDeleteCommand) {
        val success = clusterClient.connectAsService {
            certificateSigningRequests().delete(command.certificateSigningRequest)
        }

        if (!success)
            throw StatusException(
                HttpStatusCode.InternalServerError,
                "Certificate Signing Request '${command.certificateSigningRequest.metadata.name}' could not be deleted."
            )
    }

}