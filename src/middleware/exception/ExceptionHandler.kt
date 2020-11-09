package cozy.middleware.exception

import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.Query
import cozy.middleware.exception.data.ExceptionDetails
import cozy.middleware.exception.requests.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.valiktor.ConstraintViolationException

@KoinApiExtension
object ExceptionHandler: KoinComponent {
    private val bus: CommandBus by inject(named<ExceptionBus>())

    fun StatusPages.Configuration.exceptionHandler() {
        // Interceptor function for handling StatusExceptions
        // In the case that this is triggered the user has probably done something wrong
        exception<StatusException> {
            var query: Query<ExceptionDetails> = StatusExceptionQuery(it)
            if (it.status == HttpStatusCode.InternalServerError)
                query = ThrowableQuery(it)

            val result = bus.executeQuery(query)
            call.respond(result.status, result)
        }

        // Interceptor function for handling ConstraintViolationException
        // In the case that this is triggered the user has passed invalid data
        exception<ConstraintViolationException> {
            val query = ConstraintViolationExceptionQuery(it)

            val result = bus.executeQuery(query)
            call.respond(result.status, result)
        }

        // Interceptor function for handling any unhandled Exceptions
        // In the case that this is triggered we should investigate the cause
        exception<Throwable> {
            val query = ThrowableQuery(it)

            val result = bus.executeQuery(query)
            call.respond(result.status, result)
        }
    }
}

