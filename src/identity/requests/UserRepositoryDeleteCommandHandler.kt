package cozy.identity.requests

import com.trendyol.kediatr.AsyncCommandHandler
import cozy.exception.middleware.StatusException
import cozy.identity.repositories.SigningRequestRepository
import io.ktor.http.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class UserRepositoryDeleteCommandHandler: AsyncCommandHandler<UserRepositoryDeleteCommand>, KoinComponent {

    private val signingRequestRepository: SigningRequestRepository by inject()

    override suspend fun handleAsync(command: UserRepositoryDeleteCommand) {
        val signingRequest = signingRequestRepository.retrieve(command.userIdentity.id)
            ?: throw StatusException(
                HttpStatusCode.NotFound,
                "Signing Request for '${command.userIdentity.id}' does not exists."
            )

        signingRequestRepository.delete(signingRequest.certificateSigningRequest)
    }

}