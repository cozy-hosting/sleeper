package identity.requests

import com.nhaarman.mockitokotlin2.*
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.CommandBusBuilder
import cozy.identity.repositories.UserRepository
import cozy.identity.repositories.UserRepositoryImpl
import cozy.identity.requests.IdentityBus
import cozy.identity.requests.UserEndpointCreateCommand
import cozy.identity.requests.UserEndpointDeleteCommand
import cozy.services.cluster.data.ClusterUser
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock
import org.mockito.Mockito
import java.util.*

@KoinApiExtension
class UserEndpointDeleteCommandHandlerShould: KoinTest {

    // BEGIN: Dependency injection
    private val commandBus: CommandBus by inject(named<IdentityBus>())
    private val userRepository: UserRepository by inject()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module {
            single<UserRepository> { UserRepositoryImpl() }

            single(named<IdentityBus>()) { CommandBusBuilder(UserEndpointDeleteCommand::class.java).build() }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProviderExtension = MockProviderExtension.create { Mockito.mock(it.java) }
    // END: Dependency injection

    // BEGIN: Tests
    @Test
    fun `successfully delete user if the input data is valid`(): Unit = runBlocking {
        // Arrange
        val someValidUserId = UUID.randomUUID().toString()

        declareMock<UserRepository> {
            given(runBlocking { retrieve(someValidUserId) })
                .will { ClusterUser(someValidUserId, "Max Mustermann", arrayOf("Customer")) }

            given(runBlocking{ delete(any()) })
                .will { true }
        }

        // Act
        val deleteCommand = UserEndpointDeleteCommand(someValidUserId)
        commandBus.executeCommandAsync(deleteCommand)

        // Assert
        verify(userRepository, times(1)).retrieve(someValidUserId)
        verify(userRepository, times(1)).delete(any())
    }
    // END: Tests

}