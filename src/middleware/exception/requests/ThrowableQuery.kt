package cozy.middleware.exception.requests

import com.trendyol.kediatr.Query
import cozy.middleware.exception.data.ExceptionDetails

class ThrowableQuery(val throwable: Throwable): Query<ExceptionDetails>