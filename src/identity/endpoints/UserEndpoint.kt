package cozy.identity.endpoints

import com.trendyol.kediatr.CommandBus
import cozy.identity.data.UserCreateDto
import cozy.identity.requests.UserEndpointCreateCommand
import cozy.identity.requests.IdentityBus
import cozy.identity.requests.UserEndpointRetrieveQuery
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

    @Location("{id}")
    class Retrieve(val id: String)

    @Location("")
    class Create

    private val commandBus: CommandBus by inject(named<IdentityBus>())

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

}