package cozy.repositories

import cozy.repositories.cert.SigningRequestRepository
import cozy.repositories.cert.SigningRequestRepositoryImpl
import cozy.repositories.jobs.JobRepository
import cozy.repositories.jobs.JobRepositoryImpl
import cozy.repositories.namespace.NamespaceRepository
import cozy.repositories.namespace.NamespaceRepositoryImpl
import cozy.repositories.user.UserRepository
import cozy.repositories.user.UserRepositoryImpl
import org.koin.core.KoinApplication
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@KoinApiExtension
fun KoinApplication.repositories() {
    val services = module(createdAtStart = true) {
        single<NamespaceRepository> { NamespaceRepositoryImpl() }
        single<SigningRequestRepository> { SigningRequestRepositoryImpl() }
        single<JobRepository> { JobRepositoryImpl() }
        single<UserRepository> { UserRepositoryImpl() }
    }

    modules(services)
}