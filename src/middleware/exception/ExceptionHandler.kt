package cozy.middleware.exception

import com.trendyol.kediatr.CommandBus
import cozy.middleware.exception.requests.ExceptionBus
import cozy.middleware.exception.requests.StatusExceptionQuery
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

object ExceptionHandler: KoinComponent {
    private val bus: CommandBus by inject(named<ExceptionBus>())

    fun StatusPages.Configuration.exceptionHandler() {
        exception<StatusException> {
            val query = StatusExceptionQuery(it)
            val result = bus.executeQuery(query)

            call.respond(result.status, result)
        }
    }
}

