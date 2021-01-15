package cozy.jobs.services

import cozy.repositories.jobs.data.AbstractJob
import io.fabric8.kubernetes.api.model.Namespace

interface JobService {

    suspend fun waitUntilCondition(abstractJob: AbstractJob, block: AbstractJob.() -> Boolean)

    suspend fun waitUntilSucceeded(abstractJob: AbstractJob)

}