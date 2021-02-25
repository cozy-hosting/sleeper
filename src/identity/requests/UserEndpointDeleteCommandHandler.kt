package cozy.identity.requests

import com.trendyol.kediatr.AsyncCommandHandler
import cozy.identity.repositories.UserRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class UserEndpointDeleteCommandHandler: AsyncCommandHandler<UserEndpointDeleteCommand>, KoinComponent {

    private val userRepository: UserRepository by inject()

    override suspend fun handleAsync(command: UserEndpointDeleteCommand) {
        val userIdentity = userRepository.retrieve(command.id)

        userRepository.delete(userIdentity)
    }

}