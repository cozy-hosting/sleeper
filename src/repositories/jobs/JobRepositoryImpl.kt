package cozy.repositories.jobs

import cozy.services.cluster.ServiceClusterClient
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.batch.Job
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class JobRepositoryImpl : JobRepository, KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun retrieveAll(namespace: Namespace): List<Job> = coroutineScope {
        clusterClient.connectAsService {
            batch().jobs().inNamespace(namespace.metadata.name).list().items
        }
    }

    override suspend fun retrieve(namespace: Namespace, name: String): Job = coroutineScope {
        clusterClient.connectAsService {
            batch().jobs().inNamespace(namespace.metadata.name).withName(name).get()
        }
    }

    override suspend fun create(namespace: Namespace, job: Job): Job = coroutineScope {
        clusterClient.connectAsService {
            batch().jobs().inNamespace(namespace.metadata.name).create(job)
        }
    }

    override suspend fun delete(namespace: Namespace, job: Job): Boolean = coroutineScope {
        clusterClient.connectAsService {
            batch().jobs().inNamespace(namespace.metadata.name).delete(job)
        }
    }

}