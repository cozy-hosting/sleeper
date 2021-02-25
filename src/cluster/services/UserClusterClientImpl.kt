package cozy.cluster.services

import cozy.services.cluster.data.ClusterClientConfig
import cozy.services.cluster.data.ClusterUser
import io.fabric8.kubernetes.client.Config
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import io.fabric8.kubernetes.client.KubernetesClient
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class UserClusterClientImpl(private val config: ClusterClientConfig): UserClusterClient, KoinComponent {

    private val connector: ClusterClientConnector by inject()

    override suspend fun <T> connectAsUser(user: ClusterUser, block: KubernetesClient.() -> T): T {
        val customConfig = Config.empty()
        customConfig.isTrustCerts = true
        customConfig.masterUrl = config.masterUrl
        customConfig.caCertFile = config.caCertFile

        if (user.clientCert == null && user.clientKey == null)
            throw IllegalArgumentException("Both a client cert and private key must be provided alongside the user")

        customConfig.username = user.name
        customConfig.clientCertData = user.clientCert
        customConfig.clientKeyData = user.clientKey

        val userSpecificConfig = ConfigBuilder(customConfig).build()
        val client = DefaultKubernetesClient(userSpecificConfig)
        return connector.connect(client, block)
    }

}