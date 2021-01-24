package cozy.identity.requests

import com.trendyol.kediatr.AsyncQueryHandler
import cozy.cluster.services.ServiceClusterClient
import cozy.identity.data.SigningRequest
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class SigningRequestRepositoryRetrieveAllQueryHandler :
    AsyncQueryHandler<SigningRequestRepositoryRetrieveAllQuery, List<SigningRequest>>, KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun handleAsync(query: SigningRequestRepositoryRetrieveAllQuery): List<SigningRequest> {
        val response = clusterClient.connectAsService {
            certificateSigningRequests().list().items
        } ?: listOf<CertificateSigningRequest>()

        return response.drop(query.drop).take(query.take)
            .map { SigningRequest(it) }
    }

}