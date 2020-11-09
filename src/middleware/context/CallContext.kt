package cozy.middleware.context

import io.ktor.application.*
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class CallContext(val call: ApplicationCall?): AbstractCoroutineContextElement(CallContext) {

    companion object Key: CoroutineContext.Key<CallContext>

}