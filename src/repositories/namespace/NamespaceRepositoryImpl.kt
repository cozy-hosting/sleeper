package cozy.repositories.namespace

import cozy.services.cluster.ServiceClusterClient
import io.fabric8.kubernetes.api.model.Namespace
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class NamespaceRepositoryImpl : NamespaceRepository, KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun retrieveAll(): List<Namespace> {
        return clusterClient.connectAsService {
            namespaces().list().items
        }
    }

    override suspend fun retrieve(name: String): Namespace {
        return clusterClient.connectAsService {
            namespaces().withName(name).get()
        }
    }

    override suspend fun create(namespace: Namespace): Namespace {
        return clusterClient.connectAsService {
            namespaces().create(namespace)
        }
    }

    override suspend fun delete(namespace: Namespace): Boolean {
        return clusterClient.connectAsService {
            namespaces().delete(namespace)
        }
    }

}