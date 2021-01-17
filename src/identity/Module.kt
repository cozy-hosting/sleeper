package cozy.identity

import cozy.identity.repositories.SigningRequestRepository
import cozy.identity.repositories.SigningRequestRepositoryImpl
import cozy.identity.repositories.UserRepository
import cozy.identity.repositories.UserRepositoryImpl
import cozy.identity.services.CertificateService
import cozy.identity.services.CertificateServiceImpl
import cozy.identity.services.SigningRequestService
import cozy.identity.services.SigningRequestServiceImpl
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

