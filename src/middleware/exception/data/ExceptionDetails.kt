package cozy.middleware.exception.data

import io.ktor.http.*
import java.util.*

data class ExceptionDetails(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val status: HttpStatusCode,
    val type: String,
    val details: Any
)