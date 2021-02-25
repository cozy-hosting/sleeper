package cozy.exception.middleware

import com.trendyol.kediatr.CommandBusBuilder
import cozy.exception.requests.ExceptionBus
import cozy.exception.requests.StatusExceptionQuery
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun KoinApplication.exception() {
    val exceptionModule = module(createdAtStart = true) {
        // CommandBus associated with the exception handling middleware
        single(named<ExceptionBus>()) { CommandBusBuilder(StatusExceptionQuery::class.java).build() }
    }

    modules(exceptionModule)
}