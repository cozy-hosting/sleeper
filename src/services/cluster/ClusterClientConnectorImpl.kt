package cozy.services.cluster

import cozy.middleware.exception.StatusException
import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.KubernetesClientException
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import java.net.ConnectException
import java.net.UnknownHostException

class ClusterClientConnectorImpl: ClusterClientConnector {

    override suspend fun <T> connect(client: KubernetesClient, block: KubernetesClient.() -> T): T = coroutineScope {
        try {
            // Retrieve a connection from the underlying fabric8 Kubernetes Client
            // and auto dispose it once the query has been terminated
            client.use { connection ->
                block(connection)
            }
        } catch (ex: KubernetesClientException) {
            if (ex.status?.code == HttpStatusCode.Forbidden.value)
                throw StatusException(
                    HttpStatusCode.Forbidden,
                    ex.status.message.replace("\"", "'").capitalize()
                )

            // If the cause of the clients exception is not an unknown host simply
            // return the client. The occurring problem must be handled somewhere else
            if (ex.cause !is UnknownHostException && ex.cause !is ConnectException)
                throw ex

            // Inform the user that he is not the cause of the problem
            throw StatusException(
                HttpStatusCode.InternalServerError,
                "Kubernetes API on '${client.configuration.masterUrl}' is unreachable"
            )
        }
    }

}