package cozy.middleware.exception.requests

import com.trendyol.kediatr.Query
import cozy.middleware.exception.data.ExceptionDetails
import org.valiktor.ConstraintViolationException

@Suppress("Unused")
class ConstraintViolationExceptionQuery(val constraintViolationException: ConstraintViolationException): Query<ExceptionDetails>