package cozy.services.cert

import cozy.repositories.cert.data.SigningRequest
import cozy.repositories.jobs.JobRepository
import cozy.repositories.namespace.NamespaceRepository
import cozy.services.cert.jobs.ApprovalJob
import cozy.services.cluster.ServiceClusterClient
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

@KoinApiExtension
class SigningRequestServiceImpl : SigningRequestService, KoinComponent {

    private val namespaceRepository: NamespaceRepository by inject()
    private val jobRepository: JobRepository by inject()

    private val clusterClient: ServiceClusterClient by inject()

    override suspend fun approve(signingRequest: SigningRequest): Boolean = coroutineScope {
        val default = namespaceRepository.retrieve("default")

        val approval = ApprovalJob("approve-${signingRequest.name}", signingRequest.name)
        jobRepository.create(default, approval.job)

        // TODO: Refactor this into its own service ...
        clusterClient.connectAsService {
            batch().jobs()
                .inNamespace(default.metadata.name)
                .withName(approval.job.metadata.name)
                .waitUntilCondition({
                    if (it.status.succeeded == null)
                        return@waitUntilCondition false
                    it.status.succeeded == 1
                }, 20, TimeUnit.SECONDS)
        }

        jobRepository.delete(default, approval.job)
    }

}