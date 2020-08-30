package cozy

import cozy.endpoints.endpoints
import cozy.middleware.auth.jwtBearer
import cozy.middleware.exception.ExceptionHandler.exceptionHandler
import cozy.middleware.exception.StatusException
import cozy.middleware.exception.exception
import cozy.repositories.repositories
import cozy.services.services
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.util.*
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused")
fun Application.module() {
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

    install(Authentication) {
        jwtBearer()
    }

    install(StatusPages) {
        exceptionHandler()
    }

    install(Routing) {
        // Endpoint routing goes here
    }

    routing {
        get {
            throw StatusException(message = "Hello World")
        }
    }
}