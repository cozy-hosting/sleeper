package cozy.identity.requests

import com.trendyol.kediatr.Command
import cozy.identity.data.CreateUserDto

class CreateUserCommand(val createUserDto: CreateUserDto): Command