package cozy.repositories

import cozy.repositories.namespace.NamespaceRepository
import cozy.repositories.namespace.NamespaceRepositoryImpl
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.repositories() {
    val services = module(createdAtStart = true) {
        single<NamespaceRepository> { NamespaceRepositoryImpl() }
    }

    modules(services)
}