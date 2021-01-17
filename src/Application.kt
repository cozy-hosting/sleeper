package cozy

import cozy.middleware.auth.jwtBearer
import cozy.context.middleware.Context
import cozy.exception.middleware.ExceptionHandler.exceptionHandler
import cozy.exception.middleware.exception
import cozy.identity.repositories.UserRepository
import cozy.identity.identity
import cozy.jobs.job
import cozy.namespace.namespace
import cozy.services.cluster.data.ClusterUser
import cozy.services.cluster
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.util.*
import org.koin.core.component.KoinApiExtension
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KoinApiExtension
@KtorExperimentalAPI
fun Application.module() {
    install(Context)

    install(Koin) {
        exception()

        identity()
        job()
        namespace()

        cluster()
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(StatusPages) {
        exceptionHandler()
    }

    install(Authentication) {
        jwtBearer()
    }

    install(Routing) {
        // Endpoint routing goes here
    }

    val userRepository: UserRepository by inject()

    routing {
        authenticate {
            post {
                val userIdentity = ClusterUser("16640c22-faf0-4766-813b-0494bdfe2642", "Max Mustermann", arrayOf("Customers"))

                userRepository.create(userIdentity)

                call.respond(userRepository.retrieve(userIdentity))
            }
        }
    }
}

