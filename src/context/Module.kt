package cozy.context

import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.context() {
    val exceptionModule = module(createdAtStart = true) {
    }

    modules(exceptionModule)
}