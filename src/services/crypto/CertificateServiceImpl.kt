package cozy.services.crypto

import kotlinx.coroutines.coroutineScope
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security
import javax.security.auth.x500.X500Principal

class CertificateServiceImpl: CertificateService {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    override suspend fun generateKeyPair(): KeyPair = coroutineScope {
        val generator = KeyPairGenerator
            .getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME)
        generator.initialize(2048)

        return@coroutineScope generator.generateKeyPair()
    }

    override suspend fun buildSigningRequest(keyPair: KeyPair, principal: X500Principal): PKCS10CertificationRequest = coroutineScope {
        val privateKey = keyPair.private
        val publicKey = keyPair.public

        val requestBuilder = JcaPKCS10CertificationRequestBuilder(principal, publicKey)
        val signerBuilder = JcaContentSignerBuilder("SHA256withRSA")

        val signer = signerBuilder.build(privateKey)
        return@coroutineScope requestBuilder.build(signer)
    }

}