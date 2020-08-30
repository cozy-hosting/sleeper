package cozy.requests

import com.trendyol.kediatr.CommandBusBuilder
import cozy.requests.helloworld.HelloWorldBus
import cozy.requests.helloworld.HelloWorldQuery
import cozy.requests.test.TestBus
import cozy.requests.test.TestQuery
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun KoinApplication.requests() {
    val services = module(createdAtStart = true) {
        single(named<HelloWorldBus>()) { CommandBusBuilder(HelloWorldQuery::class.java).build() }
        single(named<TestBus>()) { CommandBusBuilder(TestQuery::class.java).build() }
    }

    modules(services)
}