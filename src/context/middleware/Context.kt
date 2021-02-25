package cozy.context.middleware

import io.ktor.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class Context {

    class Configuration

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, Context> {
        override val key = AttributeKey<Context>("Context")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): Context {
            val feature = Context()

            // Intercept the call pipeline where we want to inject the ApplicationCallContext,
            // so that we can access it from any object in the call chain later on
            pipeline.intercept(ApplicationCallPipeline.Call) {
                feature.interceptCall(this)
            }

            return feature
        }
    }

    private suspend fun interceptCall(pipeline: PipelineContext<Unit, ApplicationCall>) {
        // Get the current context of the executing coroutine
        val currentCoroutineContext = coroutineContext

        // Build the callContext with from the current ApplicationCall
        val callContext = CallContext(pipeline.context)

        // Spawn a sub-coroutine that takes a new context constructed
        // from both the current and the call context
        withContext(currentCoroutineContext + callContext) {
            // Proceed with the pipeline
            // execution in the new context
            pipeline.proceed()
        }
    }

}

