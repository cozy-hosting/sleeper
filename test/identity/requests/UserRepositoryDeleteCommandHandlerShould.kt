package identity.requests

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.CommandBusBuilder
import cozy.exception.middleware.StatusException
import cozy.identity.data.SigningRequest
import cozy.identity.repositories.SigningRequestRepository
import cozy.identity.repositories.SigningRequestRepositoryImpl
import cozy.identity.requests.IdentityBus
import cozy.identity.requests.UserRepositoryDeleteCommand
import cozy.services.cluster.data.ClusterUser
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequestBuilder
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock
import org.mockito.Mockito
import java.util.*

@KoinApiExtension
class UserRepositoryDeleteCommandHandlerShould: KoinTest {

    // BEGIN: Dependency injection
    private val commandBus: CommandBus by inject(named<IdentityBus>())
    private val signingRequestRepository: SigningRequestRepository by inject()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module {
            single<SigningRequestRepository> { SigningRequestRepositoryImpl() }

            single(named<IdentityBus>()) { CommandBusBuilder(UserRepositoryDeleteCommand::class.java).build() }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProviderExtension = MockProviderExtension.create { Mockito.mock(it.java) }
    // END: Dependency injection

    // BEGIN: Tests
    @Test
    fun `successfully delete a SigningRequest if the input data is valid`(): Unit = runBlocking {
        // Arrange
        // Values are dependent on the respective certificates
        val someValidUserId = "ade23826-c03f-42a5-98db-49ad89a6a5de"
        val someValidUserName = "Max Mustermann"
        val someValidUserGroups = arrayOf("Customers")
        val someValidClientKey = this::class.java.getResource("/client.key").readText()
        val someValidClientKeyAsBase64 = String(Base64.getEncoder().encode(someValidClientKey.toByteArray()))

        val someValidUserIdentity = ClusterUser(
            someValidUserId,
            someValidUserName,
            someValidUserGroups
        )

        declareMock<SigningRequestRepository> {
            given(runBlocking { retrieve(someValidUserId) })
                .will {
                    val certificateSigningRequest = CertificateSigningRequestBuilder()
                        .withNewMetadata()
                        .withName(someValidUserId)
                        .addToAnnotations(SigningRequest.CLIENT_KEY, someValidClientKeyAsBase64)
                        .endMetadata()
                        .build()

                    SigningRequest(certificateSigningRequest)
                }

            given(runBlocking { delete(any()) })
                .will { true }
        }

        // Act
        val deleteCommand = UserRepositoryDeleteCommand(someValidUserIdentity)
        commandBus.executeCommandAsync(deleteCommand)

        // Assert
        verify(signingRequestRepository, times(1)).retrieve(someValidUserId)
        verify(signingRequestRepository, times(1)).delete(any())
    }

    @Test
    fun `throw StatusException if the signing request does not exist`() {
        // Arrange
        val someInvalidUserIdentity = ClusterUser(
            UUID.randomUUID().toString(),
            "Max Mustermann",
            arrayOf("Customers")
        )

        declareMock<SigningRequestRepository> {
            given(runBlocking { retrieve(someInvalidUserIdentity.id) })
                .will { null }
        }

        // Act
        val deleteCommandLambda: () -> Unit = {
            val deleteCommand = UserRepositoryDeleteCommand(someInvalidUserIdentity)
            runBlocking { commandBus.executeCommandAsync(deleteCommand) }
        }

        // Assert
        val e = Assertions.assertThrows(StatusException::class.java, deleteCommandLambda)
        Assertions.assertEquals(HttpStatusCode.NotFound, e.status)
    }
    // END: Tests
}