package cozy.middleware.exception.requests

import com.trendyol.kediatr.Query
import cozy.middleware.exception.data.ExceptionDetails
import cozy.middleware.exception.StatusException

@Suppress("Unused")
class StatusExceptionQuery(val statusException: StatusException): Query<ExceptionDetails>