package identity.extensions

import identity.asserts.PemFormatAssert
import cozy.services.cluster.data.ClusterUser
import cozy.identity.services.CertificateService
import cozy.identity.services.CertificateServiceImpl
import cozy.auth.services.toPemString
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import javax.security.auth.x500.X500Principal

class PemExtensionsTest: KoinTest {

    // BEGIN: Dependency injection
    private val certificateService: CertificateService = CertificateServiceImpl()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module {
            single<CertificateService> { CertificateServiceImpl() }
        })
    }
    // END: Dependency injection

    // BEGIN: Tests
    @Test
    fun `test private key matches pem format`(): Unit = runBlocking {
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
    fun `test certificate signing requests matches pem format`(): Unit = runBlocking {
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