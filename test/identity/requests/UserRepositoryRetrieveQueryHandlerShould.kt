package identity.requests

import com.nhaarman.mockitokotlin2.given
import com.trendyol.kediatr.CommandBus
import com.trendyol.kediatr.CommandBusBuilder
import cozy.exception.middleware.StatusException
import cozy.identity.data.SigningRequest
import cozy.identity.repositories.SigningRequestRepository
import cozy.identity.repositories.SigningRequestRepositoryImpl
import cozy.identity.requests.IdentityBus
import cozy.identity.requests.UserRepositoryRetrieveQuery
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequestBuilder
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
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
class UserRepositoryRetrieveQueryHandlerShould : KoinTest {

    // BEGIN: Dependency injection
    private val commandBus: CommandBus by inject(named<IdentityBus>())

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module {
            single<SigningRequestRepository> { SigningRequestRepositoryImpl() }

            single(named<IdentityBus>()) { CommandBusBuilder(UserRepositoryRetrieveQuery::class.java).build() }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProviderExtension = MockProviderExtension.create { Mockito.mock(it.java) }
    // END: Dependency injection

    // BEGIN: Tests
    @Test
    fun `return a ClusterUser if there is an approved signing request`(): Unit = runBlocking {
        // Arrange
        // Values are dependent on the respective certificates
        val someValidUserId = "ade23826-c03f-42a5-98db-49ad89a6a5de"
        val someValidUserName = "Max Mustermann"
        val someValidUserGroups = arrayOf("Customers")
        val someValidClientCert = this::class.java.getResource("/client.crt").readText()
        val someValidClientCertAsBase64 = String(Base64.getEncoder().encode(someValidClientCert.toByteArray()))
        val someValidClientKey = this::class.java.getResource("/client.key").readText()
        val someValidClientKeyAsBase64 = String(Base64.getEncoder().encode(someValidClientKey.toByteArray()))

        declareMock<SigningRequestRepository> {
            given(runBlocking { retrieve(someValidUserId) })
                .will {
                    val certificateSigningRequest = CertificateSigningRequestBuilder()
                        .withNewMetadata()
                        .withName(someValidUserId)
                        .addToAnnotations(SigningRequest.CLIENT_KEY, someValidClientKeyAsBase64)
                        .endMetadata()
                        .withNewStatus()
                        .withCertificate(someValidClientCertAsBase64)
                        .endStatus()
                        .build()

                    SigningRequest(certificateSigningRequest)
                }
        }

        // Act
        val retrieveQuery = UserRepositoryRetrieveQuery(someValidUserId)
        val userIdentity = commandBus.executeQueryAsync(retrieveQuery)

        // Assert
        assertEquals(someValidUserId, userIdentity.id)
        assertEquals(someValidUserName, userIdentity.name)
        assertArrayEquals(someValidUserGroups, userIdentity.groups)
        assertEquals(someValidClientCert, userIdentity.clientCert)
        assertEquals(someValidClientKey, userIdentity.clientKey)
    }

    @Test
    fun `throw a StatusException if there is no signing request existing`() {
        // Arrange
        val someNonExistingUserId = UUID.randomUUID().toString()

        declareMock<SigningRequestRepository> {
            given(runBlocking { retrieve(someNonExistingUserId) })
                .will { null }
        }

        // Act
        val retrieveQueryLambda: () -> Unit = {
            val retrieveQuery = UserRepositoryRetrieveQuery(someNonExistingUserId)
            runBlocking { commandBus.executeQueryAsync(retrieveQuery) }
        }

        // Assert
        val e = assertThrows(StatusException::class.java, retrieveQueryLambda)
        assertEquals(HttpStatusCode.NotFound, e.status)
    }

    @Test
    fun `throw a StatusException if the signing request is not approved`() {
        // Arrange
        // Values are dependent on the respective certificates
        val someValidUserId = "ade23826-c03f-42a5-98db-49ad89a6a5de"
        val someValidClientKey = this::class.java.getResource("/client.key").readText()
        val someValidClientKeyAsBase64 = String(Base64.getEncoder().encode(someValidClientKey.toByteArray()))

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
        }

        // Act
        val retrieveQueryLambda: () -> Unit = {
            val retrieveQuery = UserRepositoryRetrieveQuery(someValidUserId)
            runBlocking { commandBus.executeQueryAsync(retrieveQuery) }
        }

        // Assert
        val e = assertThrows(StatusException::class.java, retrieveQueryLambda)
        assertEquals(HttpStatusCode.Accepted, e.status)
    }
    // END: Tests

}