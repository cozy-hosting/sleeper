package cozy.identity.repositories

import cozy.auth.repositories.data.SigningRequest
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest

interface SigningRequestRepository {

    suspend fun retrieve(name: String): SigningRequest?

    suspend fun create(certificateSigningRequest: CertificateSigningRequest): SigningRequest

    suspend fun delete(certificateSigningRequest: CertificateSigningRequest): Boolean

}