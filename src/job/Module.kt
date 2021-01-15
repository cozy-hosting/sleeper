package cozy.jobs

import cozy.jobs.repository.JobRepository
import cozy.jobs.repository.JobRepositoryImpl
import cozy.jobs.services.JobService
import cozy.jobs.services.JobServiceImpl
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
