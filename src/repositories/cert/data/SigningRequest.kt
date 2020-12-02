package cozy.repositories.cert.data

import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate
import java.util.*

data class SigningRequest(private val request: CertificateSigningRequest) {

    companion object {
        const val CLIENT_KEY = "clientKey"
    }

    init {
        validate(this) {
            validate(SigningRequest::request).isNotNull()

            validate(SigningRequest::name).isNotBlank()
        }
    }

    val name = request.metadata.name!!

    val clientKey by lazy {
        val clientKeyAsBase64 = request.metadata.annotations[CLIENT_KEY]!!
        val clientKeyAsByteArray = Base64.getDecoder().decode(clientKeyAsBase64)

        String(clientKeyAsByteArray)
    }

    val clientCert by lazy {
        val clientCertAsBase64 = request.status?.certificate ?: return@lazy null
        val clientCertAsByteArray = Base64.getDecoder().decode(clientCertAsBase64)

        String(clientCertAsByteArray)
    }

}
