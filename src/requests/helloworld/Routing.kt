package cozy.requests.helloworld

import com.trendyol.kediatr.CommandBus
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Routing.requestsHelloWorld() {
    val bus by inject<CommandBus>(named<HelloWorldBus>())

    get("/helloworld") {
        val query = HelloWorldQuery()
        val response = bus.executeQuery(query)

        call.respond(response)
    }
}