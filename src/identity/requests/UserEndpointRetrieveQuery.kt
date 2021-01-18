package cozy.identity.requests

import com.trendyol.kediatr.Query
import cozy.identity.data.UserDto

class UserEndpointRetrieveQuery(val id: String): Query<UserDto>