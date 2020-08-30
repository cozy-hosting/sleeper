package cozy.services

import cozy.services.helloworld.HelloWorldService
import cozy.services.helloworld.HelloWorldServiceImpl
import org.koin.core.KoinApplication
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

fun KoinApplication.services() {
    val services = module(createdAtStart = true) {
        singleBy<HelloWorldService, HelloWorldServiceImpl>()
    }

    modules(services)
}