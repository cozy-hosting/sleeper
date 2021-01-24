package cozy.identity

import com.trendyol.kediatr.CommandBusBuilder
import cozy.identity.endpoints.UserEndpoint.createUser
import cozy.identity.endpoints.UserEndpoint.deleteUser
import cozy.identity.endpoints.UserEndpoint.retrieveAllUsers
import cozy.identity.endpoints.UserEndpoint.retrieveUser
import cozy.identity.repositories.SigningRequestRepository
import cozy.identity.repositories.SigningRequestRepositoryImpl
import cozy.identity.repositories.UserRepository
import cozy.identity.repositories.UserRepositoryImpl
import cozy.identity.requests.UserEndpointCreateCommand
import cozy.identity.requests.IdentityBus
import cozy.identity.services.CertificateService
import cozy.identity.services.CertificateServiceImpl
import cozy.identity.services.SigningRequestService
import cozy.identity.services.SigningRequestServiceImpl
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.koin.core.KoinApplication
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.dsl.module

@KtorExperimentalAPI
@KoinApiExtension
fun KoinApplication.identity() {
    val identityModule = module(createdAtStart = true) {
        single<SigningRequestRepository> { SigningRequestRepositoryImpl() }
        single<UserRepository> { UserRepositoryImpl() }

        single<CertificateService> { CertificateServiceImpl() }
        single<SigningRequestService> { SigningRequestServiceImpl() }

        single(named<IdentityBus>()) { CommandBusBuilder(UserEndpointCreateCommand::class.java).build() }
    }

    modules(identityModule)
}

@KoinApiExtension
@KtorExperimentalLocationsAPI
fun Route.identity() {
    route("/user") {
        retrieveAllUsers()
        retrieveUser()
        createUser()
        deleteUser()
    }
}
