package services.crypto

import asserts.PemFormatAssert
import cozy.services.cluster.ClusterUser
import cozy.services.crypto.CertificateService
import cozy.services.crypto.CertificateServiceImpl
import cozy.services.crypto.toPemString
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
        val userIdentity = ClusterUser("John Appleseed", arrayOf("Developers", "Administrators"))
        val principal = X500Principal(userIdentity.toString())
        val signingRequest = certificateService.buildSigningRequest(keyPair, principal)

        // Act
        val signingRequestInPemFormat = signingRequest.toPemString()

        // Assert
        val signingRequestSubject = "NEW CERTIFICATE REQUEST"
        PemFormatAssert.assertThat(signingRequestInPemFormat).isInPemFormat(signingRequestSubject)
    }
    // END: Tests

}