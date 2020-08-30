package cozy

import cozy.requests.helloworld.requestsHelloWorld
import cozy.requests.helloworld.requestsHelloworld
import cozy.requests.test.requestsTest
import cozy.services.helloworld.servicesHelloworld
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(Koin) {
        requestsHelloworld()
        servicesHelloworld()

        requestsTest()
    }

    install(Routing) {
        requestsHelloWorld()

        requestsTest()
    }
}

