package cozy.identity.requests

import com.trendyol.kediatr.AsyncQueryHandler
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security

@Suppress("unused")
@KoinApiExtension
class CertificateServiceGenerateQueryHandler : AsyncQueryHandler<CertificateServiceGenerateQuery, KeyPair>,
    KoinComponent {

    override suspend fun handleAsync(query: CertificateServiceGenerateQuery): KeyPair {
        Security.addProvider(BouncyCastleProvider())

        val generator = KeyPairGenerator
            .getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME)
        generator.initialize(2048)

        return generator.generateKeyPair()
    }

}