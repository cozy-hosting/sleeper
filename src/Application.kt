package cozy

import cozy.context.middleware.Context
import cozy.exception.middleware.ExceptionHandler.exceptionHandler
import cozy.exception.middleware.exception
import cozy.identity.identity
import cozy.identity.middleware.jwtBearer
import cozy.jobs.job
import cozy.namespace.namespace
import cozy.services.cluster
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinApiExtension
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@KoinApiExtension
fun Application.module() {
    install(Locations)

    install(Context)

    install(Koin) {
        exception()
        cluster()
        namespace()
        job()
        identity()
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = false
            isLenient = true
        })
    }

    install(StatusPages) {
        exceptionHandler()
    }

    install(Authentication) {
        jwtBearer()
    }

    install(Routing) {
        identity()
    }
}

