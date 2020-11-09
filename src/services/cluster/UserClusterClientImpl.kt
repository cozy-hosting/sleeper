package cozy.services.cluster

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
        val config = Config.empty()
        config.isTrustCerts = true
        config.masterUrl = this@UserClusterClientImpl.config.masterUrl
        config.caCertFile = this@UserClusterClientImpl.config.caCertFile

        if (user.clientCert == null && user.clientKey == null)
            throw IllegalArgumentException("Both a client cert and private key must be provided alongside the user")

        config.username = user.name
        config.clientCertData = user.clientCert
        config.clientKeyData = user.clientKey

        val userSpecificConfig = ConfigBuilder(config).build()
        val client = DefaultKubernetesClient(userSpecificConfig)
        return connector.connect(client, block)
    }

}