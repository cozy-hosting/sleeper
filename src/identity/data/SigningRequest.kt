package cozy.identity.data

import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate
import java.util.*

data class SigningRequest(val certificateSigningRequest: CertificateSigningRequest) {

    companion object {
        const val CLIENT_KEY = "clientKey"
    }

    init {
        validate(this) {
            validate(SigningRequest::certificateSigningRequest).isNotNull()

            validate(SigningRequest::name).isNotBlank()
        }
    }

    val name = certificateSigningRequest.metadata.name!!

    val clientKey by lazy {
        val clientKeyAsBase64 = certificateSigningRequest.metadata.annotations[CLIENT_KEY]!!
        val clientKeyAsByteArray = Base64.getDecoder().decode(clientKeyAsBase64)

        String(clientKeyAsByteArray)
    }

    val clientCert by lazy {
        val clientCertAsBase64 = certificateSigningRequest.status?.certificate ?: return@lazy null
        val clientCertAsByteArray = Base64.getDecoder().decode(clientCertAsBase64)

        String(clientCertAsByteArray)
    }

}
