package cozy.middleware.exception.requests

import com.trendyol.kediatr.QueryHandler
import cozy.middleware.exception.data.ExceptionDetails

@Suppress("Unused")
class StatusExceptionQueryHandler: QueryHandler<StatusExceptionQuery, ExceptionDetails> {
    override fun handle(query: StatusExceptionQuery): ExceptionDetails {
        val details = ExceptionDetails(
            message = query.statusException.message ?: "(null)",
            status = query.statusException.status,
            type = query.statusException::class.java.typeName.split(".").last(),
            details = "Not implemented as of now!"
        )

        println("Status error: ${details.status} - ${details.message}")

        return details
    }
}