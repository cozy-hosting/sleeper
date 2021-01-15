package cozy.services.cluster.data

import io.ktor.config.*
import io.ktor.util.*

data class ClusterClientConfig(
    val masterUrl: String,
    val caCertFile: String,
) {

    @KtorExperimentalAPI
    constructor(config: HoconApplicationConfig): this(
        masterUrl = config.property("kubernetes.masterUrl").getString(),
        caCertFile = config.property("kubernetes.caCertFile").getString()
    )

}