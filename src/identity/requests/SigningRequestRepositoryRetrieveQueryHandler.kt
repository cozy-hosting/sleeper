package cozy.identity.requests

import com.trendyol.kediatr.AsyncQueryHandler
import cozy.cluster.services.ServiceClusterClient
import cozy.identity.data.SigningRequest
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class SigningRequestRepositoryRetrieveQueryHandler: AsyncQueryHandler<SigningRequestRepositoryRetrieveQuery, SigningRequest?>, KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun handleAsync(query: SigningRequestRepositoryRetrieveQuery): SigningRequest? {
        val response = clusterClient.connectAsService {
            certificateSigningRequests().withName(query.name).get()
        } ?: return null

        return SigningRequest(response)
    }

}