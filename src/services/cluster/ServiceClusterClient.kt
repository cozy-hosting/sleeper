package cozy.services.cluster

import io.fabric8.kubernetes.client.KubernetesClient

interface ServiceClusterClient {

    suspend fun <T> connectAsService(block: KubernetesClient.() -> T): T

}