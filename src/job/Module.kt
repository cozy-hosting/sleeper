package cozy.jobs

import cozy.jobs.repository.JobRepository
import cozy.jobs.repository.JobRepositoryImpl
import cozy.job.services.JobService
import cozy.job.services.JobServiceImpl
import io.ktor.util.*
import org.koin.core.KoinApplication
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@KtorExperimentalAPI
@KoinApiExtension
fun KoinApplication.job() {
    val jobModule = module(createdAtStart = true) {
        single<JobRepository> { JobRepositoryImpl() }

        single<JobService> { JobServiceImpl() }
    }

    modules(jobModule)
}
