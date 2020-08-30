package cozy.middleware.exception.requests

import com.trendyol.kediatr.QueryHandler
import cozy.middleware.exception.data.ExceptionDetails
import org.slf4j.LoggerFactory

@Suppress("Unused")
class StatusExceptionQueryHandler: QueryHandler<StatusExceptionQuery, ExceptionDetails> {
    private val logger = LoggerFactory.getLogger(StatusExceptionQueryHandler::class.java)

    override fun handle(query: StatusExceptionQuery): ExceptionDetails {
        val details = ExceptionDetails(
            message = query.statusException.message ?: "(null)",
            status = query.statusException.status,
            type = query.statusException::class.java.typeName.split(".").last(),
            details = "Not implemented as of now!"
        )

        logger.warn("Status error: ${details.status} - ${details.message}")

        return details
    }
}