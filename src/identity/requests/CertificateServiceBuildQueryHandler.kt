package cozy.identity.requests

import com.trendyol.kediatr.AsyncQueryHandler
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import java.security.Security

@Suppress("unused")
@KoinApiExtension
class CertificateServiceBuildQueryHandler : AsyncQueryHandler<CertificateServiceBuildQuery, PKCS10CertificationRequest>,
    KoinComponent {

    override suspend fun handleAsync(query: CertificateServiceBuildQuery): PKCS10CertificationRequest {
        Security.addProvider(BouncyCastleProvider())

        val privateKey = query.keyPair.private
        val publicKey = query.keyPair.public

        val requestBuilder = JcaPKCS10CertificationRequestBuilder(query.principal, publicKey)
        val signerBuilder = JcaContentSignerBuilder("SHA256withRSA")

        val signer = signerBuilder.build(privateKey)
        return requestBuilder.build(signer)
    }

}