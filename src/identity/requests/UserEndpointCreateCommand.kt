package cozy.identity.requests

import com.trendyol.kediatr.Command
import cozy.identity.data.UserCreateDto

class UserEndpointCreateCommand(val userCreateDto: UserCreateDto): Command