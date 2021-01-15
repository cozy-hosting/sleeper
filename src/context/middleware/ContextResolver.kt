package cozy.context.middleware

import io.ktor.application.*
import kotlinx.coroutines.coroutineScope

object ContextResolver {

    suspend fun resolveCallContext(): ApplicationCall? = coroutineScope {
        coroutineContext[CallContext]?.call
    }

}