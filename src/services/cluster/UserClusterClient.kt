package cozy.services.cluster

import io.fabric8.kubernetes.client.KubernetesClient

interface UserClusterClient {

    suspend fun <T> connectAsUser(user: ClusterUser, block: KubernetesClient.() -> T): T

}