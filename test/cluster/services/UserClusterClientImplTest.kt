package cluster.services

import cozy.cluster.services.ClusterClientConnector
import cozy.cluster.services.ClusterClientConnectorImpl
import cozy.cluster.services.UserClusterClient
import cozy.cluster.services.UserClusterClientImpl
import cozy.services.cluster.data.ClusterClientConfig
import cozy.services.cluster.data.ClusterUser
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import java.lang.IllegalArgumentException

@KoinApiExtension
class UserClusterClientImplTest: KoinTest {

    // BEGIN: Test resources
    private val resourceLoader = UserClusterClientImplTest::class.java

    private val clientConfig = ClusterClientConfig(
        masterUrl = "https://127.0.0.1:8080/",
        caCertFile = resourceLoader.getResource("/ca.crt").path,
    )
    // END: Test resources

    // BEGIN: Dependency injection
    private val client: UserClusterClient by inject()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module {
            single<ClusterClientConnector> { ClusterClientConnectorImpl() }
            single<UserClusterClient> { UserClusterClientImpl(clientConfig) }
        })
    }
    // END: Dependency injection

    // BEGIN: Tests
    @Test
    @Disabled("Disabled until we have test certificates with an id embedded")
    fun `test connection is valid as user`(): Unit = runBlocking {
        // Arrange
        val userIdentity = ClusterUser(
            resourceLoader.getResource("/client.crt").readText(),
            resourceLoader.getResource("/client.key").readText(),
        )

        // Act
        client.connectAsUser(userIdentity) {
            // Assert
            Assertions.assertThat(configuration.isTrustCerts).isTrue
            Assertions.assertThat(configuration.masterUrl).isEqualTo(clientConfig.masterUrl)
            Assertions.assertThat(configuration.caCertFile).isEqualTo(clientConfig.caCertFile)
            Assertions.assertThat(configuration.clientCertData).isEqualTo(userIdentity.clientCert)
            Assertions.assertThat(configuration.clientKeyData).isEqualTo(userIdentity.clientKey)
        }
    }

    @Test
    fun `test throws on connection with invalid identity`() {
        // Arrange
        val invalidUserIdentity = ClusterUser(
            "242690ca-2ab3-49a2-9401-18b57eb5ddb7",
            "John Appleseed",
            arrayOf("Administrators"),
        )

        // Assert
        Assertions.assertThatThrownBy {
            // Act
            runBlocking {
                client.connectAsUser(invalidUserIdentity) {}
            }
        }.isExactlyInstanceOf(IllegalArgumentException::class.java)
    }
    // END: Tests

}