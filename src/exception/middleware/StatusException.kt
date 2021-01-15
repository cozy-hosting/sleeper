package cozy.exception.middleware

import io.ktor.http.*

class StatusException(val status: HttpStatusCode = HttpStatusCode.InternalServerError, message: String): Exception(message) {
    override fun getLocalizedMessage(): String {
        return "$status - ${super.getLocalizedMessage()}"
    }
}