package cozy.identity.requests

import com.trendyol.kediatr.AsyncCommandHandler
import cozy.identity.repositories.UserRepository
import cozy.services.cluster.data.ClusterUser
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class UserEndpointCreateCommandHandler: AsyncCommandHandler<UserEndpointCreateCommand>, KoinComponent {

    private val userRepository: UserRepository by inject()

    override suspend fun handleAsync(command: UserEndpointCreateCommand) {
        val userIdentity = ClusterUser(
            command.userCreateDto.id,
            command.userCreateDto.name,
            command.userCreateDto.groups
        )

        userRepository.create(userIdentity)
    }

}