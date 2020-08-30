package cozy.middleware.exception

import com.trendyol.kediatr.CommandBus
import cozy.middleware.exception.requests.ExceptionBus
import cozy.middleware.exception.requests.StatusExceptionQuery
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import org.valiktor.ConstraintViolationException

object ExceptionHandler: KoinComponent {
    private val bus: CommandBus by inject(named<ExceptionBus>())

    fun StatusPages.Configuration.exceptionHandler() {
        // Interceptor function for handling StatusExceptions
        // In the case that this is triggered the user has probably done something wrong
        exception<StatusException> {
            val query = StatusExceptionQuery(it)
            val result = bus.executeQuery(query)

            call.respond(result.status, result)
        }

        // Interceptor function for handling ConstraintViolationException
        // In the case that this is triggered the user has passed invalid data
        exception<ConstraintViolationException> {
            call.respond(HttpStatusCode.InternalServerError)
        }

        // Interceptor function for handling any unhandled Exceptions
        // In the case that this is triggered we should investigate the cause
        exception<Exception> {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

