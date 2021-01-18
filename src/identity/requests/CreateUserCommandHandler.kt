package cozy.identity.requests

import com.trendyol.kediatr.AsyncCommandHandler
import cozy.identity.repositories.UserRepository
import cozy.services.cluster.data.ClusterUser
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class CreateUserCommandHandler: AsyncCommandHandler<CreateUserCommand>, KoinComponent {

    private val userRepository: UserRepository by inject()

    override suspend fun handleAsync(command: CreateUserCommand) {
        val userIdentity = ClusterUser(
            command.createUserDto.id,
            command.createUserDto.name,
            command.createUserDto.groups
        )

        userRepository.create(userIdentity)
    }

}