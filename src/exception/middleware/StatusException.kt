package cozy.exception.middleware

import io.ktor.http.*

class StatusException(val status: HttpStatusCode, override val message: String, override val cause: Throwable? = null) :
    Exception(message, cause) {

    override fun getLocalizedMessage(): String {
        return "$status - ${super.getLocalizedMessage()}"
    }

}