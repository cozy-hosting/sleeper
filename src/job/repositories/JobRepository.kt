package cozy.jobs.repository

import cozy.repositories.jobs.data.AbstractJob
import io.fabric8.kubernetes.api.model.Namespace

interface JobRepository {

    suspend fun retrieveAll(namespace: Namespace): List<AbstractJob>

    suspend fun retrieve(namespace: Namespace, name: String): AbstractJob

    suspend fun create(abstractJob: AbstractJob): AbstractJob

    suspend fun delete(abstractJob: AbstractJob): Boolean

}