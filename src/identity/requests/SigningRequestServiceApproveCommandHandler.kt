package cozy.identity.requests

import com.trendyol.kediatr.AsyncCommandHandler
import cozy.exception.middleware.StatusException
import cozy.identity.jobs.ApprovalJob
import cozy.job.services.JobService
import cozy.jobs.repository.JobRepository
import cozy.namespace.repositories.NamespaceRepository
import io.ktor.http.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class SigningRequestServiceApproveCommandHandler : AsyncCommandHandler<SigningRequestServiceApproveCommand>,
    KoinComponent {

    private val namespaceRepository: NamespaceRepository by inject()
    private val jobRepository: JobRepository by inject()

    private val jobService: JobService by inject()

    override suspend fun handleAsync(command: SigningRequestServiceApproveCommand) {
        val default = namespaceRepository.retrieve("default")

        val approval = ApprovalJob("approve-${command.signingRequest.name}", default, command.signingRequest)

        jobRepository.create(approval)

        if (!jobService.waitUntilSucceeded(approval)) {
            jobRepository.delete(approval)

            throw StatusException(
                HttpStatusCode.InternalServerError,
                "Scheduled approval job did not complete successfully."
            )
        }

        jobRepository.delete(approval)
    }

}