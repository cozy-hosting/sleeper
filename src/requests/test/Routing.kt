package cozy.requests.test

import com.trendyol.kediatr.CommandBus
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Routing.requestsTest() {
    val bus by inject<CommandBus>(named<TestBus>())

    get("/test") {
        val query = TestQuery()
        val result = bus.executeQuery(query)

        call.respond(result)
    }
}