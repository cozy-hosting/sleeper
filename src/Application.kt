package cozy

import cozy.requests.helloworld.helloWorld
import cozy.requests.requests
import cozy.requests.test.test
import cozy.services.services
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(Koin) {
        requests()
        services()
    }

    install(Routing) {
        helloWorld()
        test()
    }
}

