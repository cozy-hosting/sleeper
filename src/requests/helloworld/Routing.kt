package cozy.requests.helloworld

import com.trendyol.kediatr.CommandBus
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Routing.helloWorld() {
    val bus by inject<CommandBus>(named<HelloWorldBus>())

    authenticate {
        get("/helloworld") {
            val query = HelloWorldQuery()
            val response = bus.executeQuery(query)

            val principal = call.authentication.principal<JWTPrincipal>()
            val subject = principal?.payload?.subject ?: "unknown"

            call.respond("$subject accessed: $response")
        }
    }
}