package cluster.services

import cozy.cluster.services.ClusterClientConnector
import cozy.cluster.services.ClusterClientConnectorImpl
import cozy.cluster.services.ServiceClusterClient
import cozy.cluster.services.ServiceClusterClientImpl
import cozy.services.cluster.data.ClusterClientConfig
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension

@KoinApiExtension
class ServiceClusterClientImplTest: KoinTest {

    // BEGIN: Test resources
    private val resourceLoader = ServiceClusterClientImplTest::class.java

    private val clientConfig = ClusterClientConfig(
        masterUrl = "https://127.0.0.1:8080/",
        caCertFile = resourceLoader.getResource("/ca.crt").path,
    )
    // END: Test resources

    // BEGIN: Dependency injection
    private val client: ServiceClusterClient by inject()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module {
            single<ClusterClientConnector> { ClusterClientConnectorImpl() }
            single<ServiceClusterClient> { ServiceClusterClientImpl(clientConfig) }
        })
    }
    // END: Dependency injection

    // BEGIN: Tests
    @Test
    fun `test connection is valid as service`(): Unit = runBlocking {
        // Arrange
        // Nothing to arrange here, since the connect method
        // in the act phase does not take any arguments

        // Act
        client.connectAsService {
            // Assert
            Assertions.assertThat(configuration.isTrustCerts).isTrue
            Assertions.assertThat(configuration.masterUrl).isEqualTo(clientConfig.masterUrl)
        }
    }
    // END: Tests

}