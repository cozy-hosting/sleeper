package cozy.repositories.jobs.data

import io.fabric8.kubernetes.api.model.batch.Job

abstract class AbstractJob(open val name: String) {

    abstract val job: Job

    val succeeded: Boolean? by lazy {
        if (job.status == null || job.status.succeeded == null)
            false
        else
            job.status.succeeded > 0
    }

}
