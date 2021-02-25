package cozy.identity.endpoints

import com.trendyol.kediatr.CommandBus
import cozy.identity.data.UserCreateDto
import cozy.identity.repositories.UserRepository
import cozy.identity.requests.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

@KoinApiExtension
@KtorExperimentalLocationsAPI
object UserEndpoint : KoinComponent {

    @Location("")
    class RetrieveAll(val drop: Int = 0, val take: Int = 10)

    @Location("{id}")
    class Retrieve(val id: String)

    @Location("")
    class Create

    @Location("{id}")
    class Delete(val id: String)

    private val commandBus: CommandBus by inject(named<IdentityBus>())

    fun Route.retrieveAllUsers() {
        get<RetrieveAll> {
            val retrieveAllQuery = UserEndpointRetrieveAllQuery(it.drop, it.take)
            val userIdentities = commandBus.executeQueryAsync(retrieveAllQuery)

            call.respond(HttpStatusCode.OK, userIdentities)
        }
    }

    fun Route.retrieveUser() {
        get<Retrieve> {
            val retrieveUserQuery = UserEndpointRetrieveQuery(it.id)
            val userIdentity = commandBus.executeQueryAsync(retrieveUserQuery)

            call.respond(HttpStatusCode.OK, userIdentity)
        }
    }

    fun Route.createUser() {
        post<Create> {
            val createUserDto = call.receive<UserCreateDto>()

            val createUserCommand = UserEndpointCreateCommand(createUserDto)
            commandBus.executeCommandAsync(createUserCommand)

            call.response.header(
                HttpHeaders.Location,
                call.request.uri + locations.href(Retrieve(createUserDto.id))
            )
            call.respond(HttpStatusCode.Created)
        }
    }

    fun Route.deleteUser() {
        delete<Delete> {
            val deleteCommand = UserEndpointDeleteCommand(it.id)
            commandBus.executeCommandAsync(deleteCommand)

            call.respond(HttpStatusCode.NoContent)
        }
    }

}