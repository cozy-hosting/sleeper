package cozy.exception.data

import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate
import java.util.*

@Serializable
data class ExceptionDetails(
    // A unique identifier associated with the given error instance
    val id: String = UUID.randomUUID().toString(),

    // The message associated with the errors exception
    val message: String,

    // The HttpStatusCode returned by the server in response
    val status: Int,

    // The type of exception triggering the error
    val type: String,

    // Further detail messages
    val details: List<String>? = null
) {
    init {
        validate(this) {
            validate(ExceptionDetails::id).isNotBlank()
            validate(ExceptionDetails::message).isNotBlank()
            validate(ExceptionDetails::status).isGreaterThanOrEqualTo(0)
            validate(ExceptionDetails::type).isNotBlank()
        }
    }

    fun toHttpStatusCode(): HttpStatusCode {
        return HttpStatusCode.fromValue(status)
    }

}