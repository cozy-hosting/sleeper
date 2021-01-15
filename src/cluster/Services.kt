package cozy.services

import com.typesafe.config.ConfigFactory
import cozy.auth.services.SigningRequestService
import cozy.auth.services.SigningRequestServiceImpl
import cozy.auth.services.CertificateService
import cozy.auth.services.CertificateServiceImpl
import cozy.cluster.services.*
import cozy.services.cluster.data.ClusterClientConfig
import cozy.jobs.services.JobService
import cozy.jobs.services.JobServiceImpl
import io.ktor.config.*
import io.ktor.util.*
import org.koin.core.KoinApplication
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@KtorExperimentalAPI
@KoinApiExtension
fun KoinApplication.cluster() {
    val applicationConfig = HoconApplicationConfig(ConfigFactory.load())

    val clusterModule = module(createdAtStart = true) {
        single<ClusterClientConnector> { ClusterClientConnectorImpl() }
        single<UserClusterClient> { UserClusterClientImpl(ClusterClientConfig(applicationConfig)) }
        single<ServiceClusterClient> { ServiceClusterClientImpl(ClusterClientConfig(applicationConfig)) }
    }

    modules(clusterModule)
}