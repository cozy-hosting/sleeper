package cozy

import cozy.endpoints.endpoints
import cozy.middleware.auth.jwtBearer
import cozy.middleware.context.Context
import cozy.middleware.exception.ExceptionHandler.exceptionHandler
import cozy.middleware.exception.exception
import cozy.repositories.repositories
import cozy.repositories.user.UserRepository
import cozy.services.cluster.data.ClusterUser
import cozy.services.services
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
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KoinApiExtension
@KtorExperimentalAPI
fun Application.module() {
    install(Context)

    install(Koin) {
        exception()

        endpoints()
        services()
        repositories()
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
            get {
                val userIdentity = ClusterUser("16640c22-faf0-4766-813b-0494bdfe2642", "Max Mustermann", arrayOf("Customers"))

                val user = userRepository.delete(userIdentity)

                call.respond(user)
            }
        }
    }
}

