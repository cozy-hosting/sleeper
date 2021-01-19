package cozy.identity.requests

import com.trendyol.kediatr.AsyncQueryHandler
import cozy.exception.middleware.StatusException
import cozy.identity.repositories.SigningRequestRepository
import cozy.services.cluster.data.ClusterUser
import io.ktor.http.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class UserRepositoryRetrieveQueryHandler : AsyncQueryHandler<UserRepositoryRetrieveQuery, ClusterUser>, KoinComponent {

    private val signingRequestRepository: SigningRequestRepository by inject()

    override suspend fun handleAsync(query: UserRepositoryRetrieveQuery): ClusterUser {
        val signingRequest = signingRequestRepository.retrieve(query.id)
            ?: throw StatusException(
                HttpStatusCode.NotFound,
                "User '${query.id}' does not exist."
            )

        if (signingRequest.clientCert == null)
            throw StatusException(HttpStatusCode.Accepted, "Signing Request has been denied, or is still pending.")

        return ClusterUser(signingRequest.clientCert!!, signingRequest.clientKey)
    }

}