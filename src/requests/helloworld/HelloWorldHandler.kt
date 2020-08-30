package cozy.requests.helloworld

import com.trendyol.kediatr.QueryHandler
import cozy.services.helloworld.HelloWorldService
import org.koin.core.KoinComponent
import org.koin.core.inject

@Suppress("Unused")
class HelloWorldHandler: QueryHandler<HelloWorldQuery, String>, KoinComponent {
    val helloWorldService by inject<HelloWorldService>()

    override fun handle(query: HelloWorldQuery): String {
        return helloWorldService.sayHelloWorld()
    }

}