package cozy.exception.requests

import com.trendyol.kediatr.Query
import cozy.exception.data.ExceptionDetails
import cozy.exception.middleware.StatusException

@Suppress("Unused")
class StatusExceptionQuery(val statusException: StatusException): Query<ExceptionDetails>