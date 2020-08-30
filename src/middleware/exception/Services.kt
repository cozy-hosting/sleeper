package cozy.middleware.exception

import com.trendyol.kediatr.CommandBusBuilder
import cozy.middleware.exception.requests.ExceptionBus
import cozy.middleware.exception.requests.StatusExceptionQuery
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun KoinApplication.exception() {
    val services = module(createdAtStart = true) {
        single(named<ExceptionBus>()) { CommandBusBuilder(StatusExceptionQuery::class.java).build() }
    }

    modules(services)
}