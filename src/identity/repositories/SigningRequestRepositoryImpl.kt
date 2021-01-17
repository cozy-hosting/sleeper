package cozy.identity.repositories

import cozy.identity.data.SigningRequest
import cozy.cluster.services.ServiceClusterClient
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class SigningRequestRepositoryImpl : SigningRequestRepository, KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun retrieve(name: String): SigningRequest? = coroutineScope {
        val response = clusterClient.connectAsService {
            certificateSigningRequests().withName(name).get()
        } ?: return@coroutineScope null

        SigningRequest(response)
    }

    override suspend fun create(certificateSigningRequest: CertificateSigningRequest): SigningRequest = coroutineScope {
        val response = clusterClient.connectAsService {
            certificateSigningRequests().create(certificateSigningRequest)
        }
        SigningRequest(response)
    }

    override suspend fun delete(certificateSigningRequest: CertificateSigningRequest): Boolean = coroutineScope {
        clusterClient.connectAsService {
            certificateSigningRequests().delete(certificateSigningRequest)
        }
    }
}