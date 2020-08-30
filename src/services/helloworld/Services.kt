package cozy.services.helloworld

import org.koin.core.KoinApplication
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

fun KoinApplication.servicesHelloworld() {
    val services = module(createdAtStart = true) {
        singleBy<HelloWorldService, HelloWorldServiceImpl>()
    }

    modules(services)
}