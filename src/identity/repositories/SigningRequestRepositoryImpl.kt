package cozy.identity.repositories

import com.trendyol.kediatr.CommandBus
import cozy.exception.middleware.StatusException
import cozy.identity.data.SigningRequest
import cozy.identity.requests.IdentityBus
import cozy.identity.requests.SigningRequestRepositoryCreateCommand
import cozy.identity.requests.SigningRequestRepositoryDeleteCommand
import cozy.identity.requests.SigningRequestRepositoryRetrieveQuery
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest
import io.ktor.http.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

@KoinApiExtension
class SigningRequestRepositoryImpl : SigningRequestRepository, KoinComponent {

    private val commandBus: CommandBus by inject(named<IdentityBus>())

    override suspend fun retrieve(name: String): SigningRequest? {
        val retrieveQuery = SigningRequestRepositoryRetrieveQuery(name)
        return commandBus.executeQueryAsync(retrieveQuery)
    }

    override suspend fun create(certificateSigningRequest: CertificateSigningRequest): SigningRequest {
        val createCommand = SigningRequestRepositoryCreateCommand(certificateSigningRequest)
        commandBus.executeCommandAsync(createCommand)

        return retrieve(certificateSigningRequest.metadata.name) ?: throw StatusException(
            HttpStatusCode.NotFound,
            "Certificate Signing Request '${certificateSigningRequest.metadata.name}' could not be created."
        )
    }

    override suspend fun delete(certificateSigningRequest: CertificateSigningRequest): Boolean {
        val deleteCommand = SigningRequestRepositoryDeleteCommand(certificateSigningRequest)
        commandBus.executeCommandAsync(deleteCommand)

        return true
    }
}