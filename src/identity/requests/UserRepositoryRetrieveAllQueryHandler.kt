package cozy.identity.requests

import com.trendyol.kediatr.AsyncQueryHandler
import cozy.identity.repositories.SigningRequestRepository
import cozy.services.cluster.data.ClusterUser
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class UserRepositoryRetrieveAllQueryHandler : AsyncQueryHandler<UserRepositoryRetrieveAllQuery, List<ClusterUser>>,
    KoinComponent {

    private val signingRequestRepository: SigningRequestRepository by inject()

    override suspend fun handleAsync(query: UserRepositoryRetrieveAllQuery): List<ClusterUser> {
        val signingRequests = signingRequestRepository.retrieveAll(query.drop, query.take)

        return signingRequests.filter { it.clientCert != null }
            .map { ClusterUser(it.clientCert!!, it.clientKey) }
    }

}