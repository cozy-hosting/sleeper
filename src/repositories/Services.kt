package cozy.repositories

import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.repositories() {
    val services = module(createdAtStart = true) {

    }

    modules(services)
}