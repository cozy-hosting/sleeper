package cozy.repositories.cert

import cozy.repositories.cert.data.SigningRequest
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest

interface SigningRequestRepository {

    suspend fun retrieve(name: String): SigningRequest

    suspend fun create(certificateSigningRequest: CertificateSigningRequest): SigningRequest

}