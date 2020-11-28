package cozy.repositories.jobs

import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.batch.Job

interface JobRepository {

    suspend fun retrieveAll(namespace: Namespace): List<Job>

    suspend fun retrieve(namespace: Namespace, name: String): Job

    suspend fun create(namespace: Namespace, job: Job): Job

    suspend fun delete(namespace: Namespace, job: Job): Boolean

}