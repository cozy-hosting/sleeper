package cozy.services

import com.typesafe.config.ConfigFactory
import cozy.services.clients.*
import cozy.services.cluster.*
import cozy.services.crypto.CertificateService
import cozy.services.crypto.CertificateServiceImpl
import io.ktor.config.*
import io.ktor.util.*
import org.koin.core.KoinApplication
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@KtorExperimentalAPI
@KoinApiExtension
fun KoinApplication.services() {
    val applicationConfig = HoconApplicationConfig(ConfigFactory.load())

    val services = module(createdAtStart = true) {
        single<ClusterClientConnector> { ClusterClientConnectorImpl() }
        single<UserClusterClient> { UserClusterClientImpl(ClusterClientConfig(applicationConfig)) }
        single<ServiceClusterClient> { ServiceClusterClientImpl(ClusterClientConfig(applicationConfig)) }

        single<CertificateService> { CertificateServiceImpl() }
    }

    modules(services)
}