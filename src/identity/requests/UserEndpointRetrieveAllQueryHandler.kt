package cozy.identity.requests

import com.trendyol.kediatr.AsyncQueryHandler
import cozy.identity.data.UserDto
import cozy.identity.repositories.UserRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
@KoinApiExtension
class UserEndpointRetrieveAllQueryHandler: AsyncQueryHandler<UserEndpointRetrieveAllQuery, List<UserDto>>, KoinComponent {

    private val userRepository: UserRepository by inject()

    override suspend fun handleAsync(query: UserEndpointRetrieveAllQuery): List<UserDto> {
        val userIdentities = userRepository.retrieveAll(query.drop, query.take)

        return userIdentities.map { UserDto(it.id, it.name, it.groups) }
    }

}