package cozy.jobs.services

import cozy.repositories.jobs.data.AbstractJob
import cozy.repositories.jobs.data.BatchJob
import cozy.cluster.services.ServiceClusterClient
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

@KoinApiExtension
class JobServiceImpl : JobService, KoinComponent {

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun waitUntilCondition(abstractJob: AbstractJob, block: AbstractJob.() -> Boolean) {
        val job = abstractJob.job

        clusterClient.connectAsService {
            batch().jobs()
                .inNamespace(job.metadata.namespace)
                .withName(job.metadata.name)
                .waitUntilCondition({ block(BatchJob(it)) }, 20, TimeUnit.SECONDS)
        }
    }

    override suspend fun waitUntilSucceeded(abstractJob: AbstractJob) {
        waitUntilCondition(abstractJob) {
            succeeded ?: false
        }
    }

}