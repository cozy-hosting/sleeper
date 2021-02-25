package cozy.namespace

import cozy.namespace.repositories.NamespaceRepository
import cozy.namespace.repositories.NamespaceRepositoryImpl
import io.ktor.util.*
import org.koin.core.KoinApplication
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@KtorExperimentalAPI
@KoinApiExtension
fun KoinApplication.namespace() {
    val namespaceModule = module(createdAtStart = true) {
        single<NamespaceRepository> { NamespaceRepositoryImpl() }
    }

    modules(namespaceModule)
}