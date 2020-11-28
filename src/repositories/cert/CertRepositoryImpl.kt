package cozy.repositories.cert

import cozy.services.cluster.ServiceClusterClient
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class CertRepositoryImpl : CertRepository, KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun retrive(name: String): CertificateSigningRequest {
        return clusterClient.connectAsService {
            certificateSigningRequests().withName(name).get()
        }
    }

    override suspend fun create(certificateSigningRequest: CertificateSigningRequest): CertificateSigningRequest {
        return clusterClient.connectAsService {
            certificateSigningRequests().create(certificateSigningRequest)
        }
    }

}