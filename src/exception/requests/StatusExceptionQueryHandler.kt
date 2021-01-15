package cozy.exception.requests

import com.trendyol.kediatr.QueryHandler
import cozy.exception.middleware.StatusException
import cozy.exception.data.ExceptionDetails
import org.slf4j.LoggerFactory

@Suppress("Unused")
class StatusExceptionQueryHandler: QueryHandler<StatusExceptionQuery, ExceptionDetails> {
    // Get a logger for the StatusException context
    private val logger = LoggerFactory.getLogger(StatusException::class.java)

    override fun handle(query: StatusExceptionQuery): ExceptionDetails {
        // Declare the ExceptionDetails from the StatusException
        val details = ExceptionDetails(
            message = query.statusException.message!!,
            status = query.statusException.status,
            type = query.statusException::class.java.typeName.split(".").last()
        )

        // Provide some information about the exception to the log
        logger.warn("Respond status \"${details.status}\" with message \"${details.message}\"")

        // Return the ExceptionDetails
        return details
    }
}