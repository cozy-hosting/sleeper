package cozy.jobs.repository

import cozy.repositories.jobs.data.AbstractJob
import cozy.repositories.jobs.data.BatchJob
import cozy.cluster.services.ServiceClusterClient
import io.fabric8.kubernetes.api.model.Namespace
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class JobRepositoryImpl : JobRepository, KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun retrieveAll(namespace: Namespace): List<AbstractJob> = coroutineScope {
        clusterClient.connectAsService {
            batch().jobs().inNamespace(namespace.metadata.name)
                .list().items.map { BatchJob(it) }
        }
    }

    override suspend fun retrieve(namespace: Namespace, name: String): BatchJob = coroutineScope {
        val result = clusterClient.connectAsService {
            batch().jobs().inNamespace(namespace.metadata.name).withName(name).get()
        }
        BatchJob(result)
    }

    override suspend fun create(abstractJob: AbstractJob): AbstractJob = coroutineScope {
        val job = abstractJob.job

        val result = clusterClient.connectAsService {
            batch().jobs().inNamespace(job.metadata.namespace).create(job)
        }
        BatchJob(result)
    }

    override suspend fun delete(abstractJob: AbstractJob): Boolean = coroutineScope {
        val job = abstractJob.job

        clusterClient.connectAsService {
            batch().jobs().inNamespace(job.metadata.namespace).delete(job)
        }
    }

}