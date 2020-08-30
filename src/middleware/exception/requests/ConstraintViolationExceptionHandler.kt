package cozy.middleware.exception.requests

import com.trendyol.kediatr.QueryHandler
import cozy.middleware.exception.data.ExceptionDetails
import io.ktor.http.*
import org.slf4j.LoggerFactory
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage
import java.util.*

@Suppress("Unused")
class ConstraintViolationExceptionHandler: QueryHandler<ConstraintViolationExceptionQuery, ExceptionDetails> {
    private val logger = LoggerFactory.getLogger(ConstraintViolationException::class.java)

    override fun handle(query: ConstraintViolationExceptionQuery): ExceptionDetails {
        val exception = query.constraintViolationException
        val violatedConstraints = exception.constraintViolations
            .mapToMessage(baseName = "messages", locale = Locale.ENGLISH)
            .map { "${it.property}: ${it.message}" }

        // Declare the ExceptionDetails from the ConstraintViolationException
        val details = ExceptionDetails(
            message = "Validation did not complete successfully",
            status = HttpStatusCode.BadRequest,
            type = exception::class.java.typeName.split(".").last(),
            details = violatedConstraints
        )

        // Provide some information about the exception to the log
        logger.warn("Validation failed for constraints: $violatedConstraints")

        // Return the ExceptionDetails
        return details
    }
}