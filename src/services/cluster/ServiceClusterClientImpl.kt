package cozy.services.cluster

import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import io.fabric8.kubernetes.client.KubernetesClient
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class ServiceClusterClientImpl(private val config: ClusterClientConfig): ServiceClusterClient, KoinComponent {

    private val connector: ClusterClientConnector by inject()

    override suspend fun <T> connectAsService(block: KubernetesClient.() -> T): T {
        val defaultConfig = ConfigBuilder()
            .withMasterUrl(this@ServiceClusterClientImpl.config.masterUrl)
            .withTrustCerts(true)
            .build()

        // Kubernetes client with default configuration provided by
        // the attached Service Account that operates this application
        // in a container or the respective config on the application host
        val client = DefaultKubernetesClient(defaultConfig)
        return connector.connect(client, block)
    }

}