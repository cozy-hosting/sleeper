package cozy.identity.repositories

import cozy.cluster.services.ServiceClusterClient
import cozy.exception.middleware.StatusException
import cozy.identity.data.SigningRequest
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest
import io.fabric8.kubernetes.client.KubernetesClientException
import io.ktor.http.*
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
        try {
            val response = clusterClient.connectAsService {
                certificateSigningRequests().create(certificateSigningRequest)
            }
            SigningRequest(response)
        } catch (e: KubernetesClientException) {
            throw StatusException(
                HttpStatusCode.BadRequest,
                "Certificate signing request '${certificateSigningRequest.metadata.name}' already exists.",
                e
            )
        }
    }

    override suspend fun delete(certificateSigningRequest: CertificateSigningRequest): Boolean = coroutineScope {
        clusterClient.connectAsService {
            certificateSigningRequests().delete(certificateSigningRequest)
        }
    }
}