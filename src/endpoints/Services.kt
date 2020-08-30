package cozy.endpoints

import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.endpoints() {
    val services = module(createdAtStart = true) {

    }

    modules(services)
}