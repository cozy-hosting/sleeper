package cozy.repositories.jobs.data

import io.fabric8.kubernetes.api.model.batch.Job

data class BatchJob(override val job: Job): AbstractJob(job.metadata.name)
