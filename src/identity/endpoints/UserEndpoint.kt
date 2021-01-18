package cozy.identity.endpoints

import com.trendyol.kediatr.CommandBus
import cozy.identity.data.CreateUserDto
import cozy.identity.requests.CreateUserCommand
import cozy.identity.requests.IdentityBus
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
object UserEndpoint: KoinComponent {

    @Location("") class Create

    private val commandBus: CommandBus by inject(named<IdentityBus>())

    fun Route.createUser() {
        post<Create> {
            val createUserDto = call.receive<CreateUserDto>()

            val createUserCommand = CreateUserCommand(createUserDto)
            commandBus.executeCommandAsync(createUserCommand)

            call.respond(HttpStatusCode.Created)
        }
    }

}