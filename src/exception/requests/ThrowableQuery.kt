package cozy.exception.requests

import com.trendyol.kediatr.Query
import cozy.exception.data.ExceptionDetails

class ThrowableQuery(val throwable: Throwable): Query<ExceptionDetails>