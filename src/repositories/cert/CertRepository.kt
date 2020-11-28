package cozy.repositories.cert

import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest

interface CertRepository {

    suspend fun retrive(name: String): CertificateSigningRequest

    suspend fun create(certificateSigningRequest: CertificateSigningRequest): CertificateSigningRequest

}