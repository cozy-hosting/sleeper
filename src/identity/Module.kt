package cozy.identity

import cozy.auth.repositories.SigningRequestRepository
import cozy.auth.repositories.SigningRequestRepositoryImpl
import cozy.auth.repositories.UserRepository
import cozy.auth.repositories.UserRepositoryImpl
import cozy.auth.services.CertificateService
import cozy.auth.services.CertificateServiceImpl
import cozy.auth.services.SigningRequestService
import cozy.auth.services.SigningRequestServiceImpl
import io.ktor.util.*
import org.koin.core.KoinApplication
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@KtorExperimentalAPI
@KoinApiExtension
fun KoinApplication.identity() {
    val identityModule = module(createdAtStart = true) {
        single<SigningRequestRepository> { SigningRequestRepositoryImpl() }
        single<UserRepository> { UserRepositoryImpl() }

        single<CertificateService> { CertificateServiceImpl() }
        single<SigningRequestService> { SigningRequestServiceImpl() }
    }

    modules(identityModule)
}

