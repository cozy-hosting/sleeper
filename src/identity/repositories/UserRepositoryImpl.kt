package cozy.identity.repositories

import com.trendyol.kediatr.CommandBus
import cozy.exception.middleware.StatusException
import cozy.identity.data.ClientAuthSigningRequest
import cozy.identity.extensions.toPemString
import cozy.identity.requests.IdentityBus
import cozy.identity.requests.UserRepositoryCreateCommand
import cozy.identity.requests.UserRepositoryDeleteCommand
import cozy.identity.requests.UserRepositoryRetrieveQuery
import cozy.identity.services.CertificateService
import cozy.identity.services.SigningRequestService
import cozy.services.cluster.data.ClusterUser
import io.fabric8.kubernetes.client.KubernetesClientException
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

@KoinApiExtension
class UserRepositoryImpl : UserRepository, KoinComponent {

    private val commandBus: CommandBus by inject(named<IdentityBus>())

    override suspend fun retrieve(id: String): ClusterUser {
        val retrieveQuery = UserRepositoryRetrieveQuery(id)
        return commandBus.executeQueryAsync(retrieveQuery)
    }

    override suspend fun create(userIdentity: ClusterUser): ClusterUser {
        val createCommand = UserRepositoryCreateCommand(userIdentity)
        commandBus.executeCommandAsync(createCommand)

        return retrieve(userIdentity.id)
    }

    override suspend fun delete(userIdentity: ClusterUser): Boolean {
        val deleteCommand = UserRepositoryDeleteCommand(userIdentity)
        commandBus.executeCommandAsync(deleteCommand)

        return true
    }

}