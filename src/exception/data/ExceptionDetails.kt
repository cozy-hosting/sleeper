package cozy.exception.data

import io.ktor.http.*
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate
import java.util.*

data class ExceptionDetails(
    // A unique identifier associated with the given error instance
    val id: String = UUID.randomUUID().toString(),

    // The message associated with the errors exception
    val message: String,

    // The HttpStatusCode returned by the server in response
    val status: HttpStatusCode,

    // The type of exception triggering the error
    val type: String,

    // Further details that are passed by a custom object
    val details: Any? = null
) {
    init {
        validate(this) {
            validate(ExceptionDetails::id).isNotBlank()
            validate(ExceptionDetails::message).isNotBlank()
            validate(ExceptionDetails::status).isNotNull()
            validate(ExceptionDetails::type).isNotBlank()
        }
    }
}