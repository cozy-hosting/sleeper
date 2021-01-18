package cozy.identity.requests

import com.trendyol.kediatr.AsyncQueryHandler
import cozy.identity.data.UserDto
import cozy.identity.repositories.UserRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class UserEndpointRetrieveQueryHandler: AsyncQueryHandler<UserEndpointRetrieveQuery, UserDto>, KoinComponent {

    private val userRepository: UserRepository by inject()

    override suspend fun handleAsync(query: UserEndpointRetrieveQuery): UserDto {
        val userIdentity = userRepository.retrieve(query.id)

        return UserDto(
            userIdentity.id,
            userIdentity.name,
            userIdentity.groups
        )
    }

}