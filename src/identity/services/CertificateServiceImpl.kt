package cozy.identity.services

import com.trendyol.kediatr.CommandBus
import cozy.identity.requests.CertificateServiceBuildQuery
import cozy.identity.requests.CertificateServiceGenerateQuery
import cozy.identity.requests.IdentityBus
import kotlinx.coroutines.coroutineScope
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security
import javax.security.auth.x500.X500Principal

@KoinApiExtension
class CertificateServiceImpl : CertificateService, KoinComponent {

    private val commandBus: CommandBus by inject(named<IdentityBus>())

    override suspend fun generateKeyPair(): KeyPair {
        val generateQuery = CertificateServiceGenerateQuery()
        return commandBus.executeQueryAsync(generateQuery)
    }

    override suspend fun buildSigningRequest(keyPair: KeyPair, principal: X500Principal): PKCS10CertificationRequest {
        val buildQuery = CertificateServiceBuildQuery(keyPair, principal)
        return commandBus.executeQueryAsync(buildQuery)
    }

}