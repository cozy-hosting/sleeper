package cozy.namespace.repositories

import cozy.cluster.services.ServiceClusterClient
import cozy.exception.middleware.StatusException
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.client.KubernetesClientException
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class NamespaceRepositoryImpl : NamespaceRepository, KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun retrieveAll(): List<Namespace> = coroutineScope {
        clusterClient.connectAsService {
            namespaces().list().items
        }
    }

    override suspend fun retrieve(name: String): Namespace = coroutineScope {
        clusterClient.connectAsService {
            namespaces().withName(name).get()
        }
    }

    override suspend fun create(namespace: Namespace): Namespace = coroutineScope {
        try {
            clusterClient.connectAsService {
                namespaces().create(namespace)
            }
        } catch (e: KubernetesClientException) {
            throw StatusException(
                HttpStatusCode.BadRequest,
                "Namespace '${namespace.metadata.name}' already exists.",
                e
            )
        }
    }

    override suspend fun delete(namespace: Namespace): Boolean = coroutineScope {
        clusterClient.connectAsService {
            namespaces().delete(namespace)
        }
    }

}