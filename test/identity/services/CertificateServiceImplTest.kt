package identity.services

import cozy.services.cluster.data.ClusterUser
import cozy.auth.services.CertificateService
import cozy.auth.services.CertificateServiceImpl
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.bouncycastle.asn1.x500.X500Name
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import java.lang.StringBuilder
import java.security.interfaces.RSAPrivateCrtKey
import java.security.interfaces.RSAPublicKey
import javax.security.auth.x500.X500Principal

class CertificateServiceImplTest: KoinTest {

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
    fun `test keypair generation successful`(): Unit = runBlocking {
        // Arrange
        // Nothing needs to be arranged here since
        // the generation of a key does not require any arguments

        // Act
        val keyPair = certificateService.generateKeyPair()
        val privateKey = keyPair.private as RSAPrivateCrtKey
        val publicKey = keyPair.public as RSAPublicKey

        // Assert
        val algorithm = "RSA"
        val keyLength = 2048

        Assertions.assertThat(privateKey.algorithm).isEqualTo(algorithm)
        Assertions.assertThat(privateKey.modulus.bitLength()).isEqualTo(keyLength)

        Assertions.assertThat(publicKey.algorithm).isEqualTo(algorithm)
        Assertions.assertThat(publicKey.modulus.bitLength()).isEqualTo(keyLength)
    }

    @Test
    fun `test csr generation successful`(): Unit = runBlocking {
        // Arrange
        val keyPair = certificateService.generateKeyPair()
        val userIdentity = ClusterUser(
            "242690ca-2ab3-49a2-9401-18b57eb5ddb7",
            "John Appleseed",
            arrayOf("Developers", "Administrators"),
        )
        val principal = X500Principal(userIdentity.toString())

        // Act
        val signingRequest = certificateService.buildSigningRequest(keyPair, principal)

        // Assert
        val subjectGroups = StringBuilder()
        userIdentity.groups.forEach { subjectGroups.append("O=${it},") }
        val subject = X500Name("CN=${userIdentity.name},OU=${userIdentity.id}," +
                "${subjectGroups.dropLast(1)}")

        Assertions.assertThat(signingRequest.subject).isEqualTo(subject)
    }
    // END: Tests

}