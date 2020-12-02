package cozy.repositories.namespace

import cozy.services.cluster.ServiceClusterClient
import io.fabric8.kubernetes.api.model.Namespace
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
        clusterClient.connectAsService {
            namespaces().create(namespace)
        }
    }

    override suspend fun delete(namespace: Namespace): Boolean = coroutineScope {
        clusterClient.connectAsService {
            namespaces().delete(namespace)
        }
    }

}