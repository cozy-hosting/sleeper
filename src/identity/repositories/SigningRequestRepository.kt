package cozy.identity.repositories

import cozy.identity.data.SigningRequest
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest

interface SigningRequestRepository {

    suspend fun retrieveAll(drop: Int = 0, take: Int = 10): List<SigningRequest>

    suspend fun retrieve(name: String): SigningRequest?

    suspend fun create(certificateSigningRequest: CertificateSigningRequest): SigningRequest

    suspend fun delete(certificateSigningRequest: CertificateSigningRequest): Boolean

}