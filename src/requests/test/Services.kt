package cozy.requests.test

import com.trendyol.kediatr.CommandBusBuilder
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun KoinApplication.requestsTest() {
    val services = module(createdAtStart = true) {
        single(named<TestBus>()) { CommandBusBuilder(TestQuery::class.java).build() }
    }

    modules(services)
}