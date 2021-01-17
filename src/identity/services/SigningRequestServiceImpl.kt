package cozy.identity.services

import cozy.auth.repositories.data.SigningRequest
import cozy.jobs.repository.JobRepository
import cozy.namespace.repositories.NamespaceRepository
import cozy.identity.jobs.ApprovalJob
import cozy.job.services.JobService
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.RuntimeException

@KoinApiExtension
class SigningRequestServiceImpl : SigningRequestService, KoinComponent {

    private val namespaceRepository: NamespaceRepository by inject()
    private val jobRepository: JobRepository by inject()

    private val jobService: JobService by inject()

    override suspend fun approve(signingRequest: SigningRequest): Boolean = coroutineScope {
        val default = namespaceRepository.retrieve("default")

        val approval = ApprovalJob("approve-${signingRequest.name}", default, signingRequest)

        jobRepository.create(approval)

        if (!jobService.waitUntilSucceeded(approval)) {
            jobRepository.delete(approval)

            throw RuntimeException("Scheduled approval job did not complete successfully.")
        }

        jobRepository.delete(approval)
    }

}