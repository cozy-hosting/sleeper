package cozy.auth.repositories.data

import cozy.auth.services.toPemString
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequestBuilder
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.valiktor.functions.isNotNull
import org.valiktor.validate
import java.util.*

data class ClientAuthSigningRequest(
    val name: String,
    val clientKey: String,
    val signingRequest: PKCS10CertificationRequest
) {

    val kubernetes: CertificateSigningRequest

    init {
        validate(this) {
            validate(ClientAuthSigningRequest::signingRequest).isNotNull()
        }

        val base64Encoder = Base64.getEncoder()

        val clientKeyAsBase64 = base64Encoder.encode(clientKey.toByteArray())
        val clientKeyString = String(clientKeyAsBase64)

        val signingRequestAsPem = signingRequest.toPemString()
        val signingRequestAsBase64 = base64Encoder.encode(signingRequestAsPem.toByteArray())
        val signingRequestString = String(signingRequestAsBase64)

        val usages = listOf("client auth")

        kubernetes = CertificateSigningRequestBuilder()
            .withNewMetadata().withName(name).addToAnnotations(SigningRequest.CLIENT_KEY, clientKeyString).endMetadata()
            .withNewSpec().withRequest(signingRequestString).withUsages(usages).endSpec()
            .build()
    }

}
