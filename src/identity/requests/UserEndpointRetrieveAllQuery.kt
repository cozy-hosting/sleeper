package cozy.identity.requests

import com.trendyol.kediatr.Query
import cozy.identity.data.UserDto

class UserEndpointRetrieveAllQuery(val drop: Int = 0, val take: Int = 10): Query<List<UserDto>>