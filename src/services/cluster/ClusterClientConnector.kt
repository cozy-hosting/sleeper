package cozy.services.cluster

import io.fabric8.kubernetes.client.KubernetesClient

interface ClusterClientConnector {

    suspend fun <T> connect(client: KubernetesClient, block: KubernetesClient.() -> T): T

}