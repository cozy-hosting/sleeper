package cozy.exception.requests

import com.trendyol.kediatr.QueryHandler
import cozy.exception.data.ExceptionDetails
import io.ktor.http.*
import org.slf4j.LoggerFactory

@Suppress("Unused")
class ThrowableHandler: QueryHandler<ThrowableQuery, ExceptionDetails> {
    // Get a logger for the Throwable context
    private val logger = LoggerFactory.getLogger(Throwable::class.java)

    override fun handle(query: ThrowableQuery): ExceptionDetails {
        // Declare the ExceptionDetails from the Throwable
        val details = ExceptionDetails(
            message = query.throwable.message ?: "Unknown reason",
            status = HttpStatusCode.InternalServerError.value,
            type = query.throwable::class.java.typeName.split(".").last()
        )

        // Provide some information about the exception to the log
        logger.error("Internal Server error occurred with id '${details.id}' for reason '${details.message}'")
        logger.error("Stack trace for the '${details.type}':\n ${query.throwable.stackTraceToString()}")

        // Return the ExceptionDetails
        return details
    }
}