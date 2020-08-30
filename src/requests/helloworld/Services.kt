package cozy.requests.helloworld

import com.trendyol.kediatr.CommandBusBuilder
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun KoinApplication.requestsHelloworld() {
    val services = module(createdAtStart = true) {
        single(named<HelloWorldBus>()) { CommandBusBuilder(HelloWorldQuery::class.java).build() }
    }

    modules(services)
}