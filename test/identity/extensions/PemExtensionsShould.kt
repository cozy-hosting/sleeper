package identity.extensions

import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.CommandBusBuilder
import identity.asserts.PemFormatAssert
import cozy.services.cluster.data.ClusterUser
import cozy.identity.services.CertificateService
import cozy.identity.services.CertificateServiceImpl
import cozy.identity.extensions.toPemString
import cozy.identity.requests.IdentityBus
import cozy.identity.requests.UserEndpointCreateCommand
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import javax.security.auth.x500.X500Principal

@KoinApiExtension
class PemExtensionsShould: KoinTest {

    // BEGIN: Dependency injection
    private val certificateService: CertificateService = CertificateServiceImpl()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module {
            single<CertificateService> { CertificateServiceImpl() }

            single(named<IdentityBus>()) { CommandBusBuilder(UserEndpointCreateCommand::class.java).build() }
        })
    }
    // END: Dependency injection

    // BEGIN: Tests
    @Test
    fun `generate valid private key in pem format`(): Unit = runBlocking {
        // Arrange
        val keyPair = certificateService.generateKeyPair()
        val privateKey = keyPair.private

        // Act
        val privateKeyInPemFormat = privateKey.toPemString()

        // Assert
        val privateKeySubject = "RSA PRIVATE KEY"
        PemFormatAssert.assertThat(privateKeyInPemFormat).isInPemFormat(privateKeySubject)
    }

    @Test
    fun `generate valid signing request in pem format`(): Unit = runBlocking {
        // Arrange
        val keyPair = certificateService.generateKeyPair()
        val userIdentity = ClusterUser(
            "242690ca-2ab3-49a2-9401-18b57eb5ddb7",
            "John Appleseed",
            arrayOf("Developers", "Administrators")
        )
        val principal = X500Principal(userIdentity.toString())
        val signingRequest = certificateService.buildSigningRequest(keyPair, principal)

        // Act
        val signingRequestInPemFormat = signingRequest.toPemString()

        // Assert
        val signingRequestSubject = "CERTIFICATE REQUEST"
        PemFormatAssert.assertThat(signingRequestInPemFormat).isInPemFormat(signingRequestSubject)
    }
    // END: Tests

}