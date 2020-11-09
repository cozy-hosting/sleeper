package cozy.middleware.context

import io.ktor.application.*
import kotlin.coroutines.coroutineContext

object ContextResolver {

    suspend fun resolveCallContext(): ApplicationCall? {
        val callContext = coroutineContext[CallContext.Key]

        return callContext?.call
    }

}