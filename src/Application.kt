package cozy

import cozy.endpoints.endpoints
import cozy.middleware.auth.jwtBearer
import cozy.middleware.context.Context
import cozy.middleware.exception.ExceptionHandler.exceptionHandler
import cozy.middleware.exception.exception
import cozy.repositories.repositories
import cozy.services.cluster.ClusterUser
import cozy.services.cluster.ServiceClusterClient
import cozy.services.crypto.CertificateService
import cozy.services.crypto.toPemString
import cozy.services.services
import io.fabric8.kubernetes.api.model.ListOptions
import io.fabric8.kubernetes.api.model.batch.Job
import io.fabric8.kubernetes.api.model.batch.JobBuilder
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequestBuilder
import io.fabric8.kubernetes.client.KubernetesClientException
import io.fabric8.kubernetes.client.Watcher
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.util.*
import kotlinx.coroutines.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Predicate
import javax.security.auth.x500.X500Principal

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KoinApiExtension
@KtorExperimentalAPI
fun Application.module() {
    install(Context)

    install(Koin) {
        exception()

        endpoints()
        services()
        repositories()
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(StatusPages) {
        exceptionHandler()
    }

    install(Authentication) {
        jwtBearer()
    }

    install(Routing) {
        // Endpoint routing goes here
    }

    val client: ServiceClusterClient by inject()
    val certService: CertificateService by inject()

    routing {
        authenticate {
            get {
                val userIdentity = ClusterUser("Max Mustermann", arrayOf("Customers"))
                val userIdentityPrincipal = X500Principal(userIdentity.toString())

                val keyPair = certService.generateKeyPair()
                val signingRequest = certService.buildSigningRequest(keyPair, userIdentityPrincipal)
                val signingRequestAsPem = signingRequest.toPemString()
                val signingRequestAsBase64 = Base64.getEncoder().encode(signingRequestAsPem.toByteArray())
                val sigintRequestString = String(signingRequestAsBase64)

                val response = client.connectAsService {
                    val request = CertificateSigningRequestBuilder()
                        .withNewMetadata()
                        .withName("user-auth-request")
                        .endMetadata()
                        .withNewSpec()
                        .withRequest(sigintRequestString)
                        .withUsages(
                            listOf(
                                "client auth"
                            )
                        )
                        .endSpec()
                        .build()

                    certificateSigningRequests().create(request)

                    val approvalJob = JobBuilder()
                        .withNewMetadata()
                        .withName("approval-request-${UUID.randomUUID().toString().subSequence(0, 5)}")
                        .endMetadata()
                        .withNewSpec()
                        .withNewTemplate()
                        .withNewSpec()
                        .addNewContainer()
                        .withName("kubectl")
                        .withImage("bitnami/kubectl:latest")
                        .withArgs("certificate", "approve", "user-auth-request")
                        .endContainer()
                        .withRestartPolicy("Never")
                        .endSpec()
                        .endTemplate()
                        .endSpec()
                        .build()

                    batch().jobs().inNamespace("default").create(approvalJob)

                    batch().jobs().inNamespace("default").withName(approvalJob.metadata.name)
                        .waitUntilCondition({
                            if (it.status.succeeded == null)
                                return@waitUntilCondition false
                            it.status.succeeded == 1
                        }, 20, TimeUnit.SECONDS)

                    batch().jobs().delete(approvalJob)

                    return@connectAsService 0
                }

                call.respond(response)
            }
        }
    }
}

