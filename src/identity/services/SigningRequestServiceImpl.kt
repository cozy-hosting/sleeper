package cozy.identity.services

import com.trendyol.kediatr.CommandBus
import cozy.exception.middleware.StatusException
import cozy.identity.data.SigningRequest
import cozy.jobs.repository.JobRepository
import cozy.namespace.repositories.NamespaceRepository
import cozy.identity.jobs.ApprovalJob
import cozy.identity.requests.IdentityBus
import cozy.identity.requests.SigningRequestServiceApproveCommand
import cozy.job.services.JobService
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.lang.RuntimeException

@KoinApiExtension
class SigningRequestServiceImpl : SigningRequestService, KoinComponent {

    private val commandBus: CommandBus by inject(named<IdentityBus>())

    override suspend fun approve(signingRequest: SigningRequest): Boolean {
        val approveCommand = SigningRequestServiceApproveCommand(signingRequest)
        commandBus.executeCommandAsync(approveCommand)

        return true
    }

}