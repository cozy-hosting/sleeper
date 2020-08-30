package cozy.requests.test

import com.trendyol.kediatr.QueryHandler

@Suppress("Unused")
class TestHandler: QueryHandler<TestQuery, String> {
    override fun handle(query: TestQuery): String {
        return "Test"
    }
}