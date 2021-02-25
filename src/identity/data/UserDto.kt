package cozy.identity.data

import kotlinx.serialization.Serializable
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate

@Serializable
class UserDto(
    val id: String,
    val name: String,
    val groups: Array<String>,
) {

    init {
        validate(this) {
            validate(UserDto::id).isNotBlank()
            validate(UserDto::name).isNotBlank()
            validate(UserDto::groups).isNotEmpty()
        }
    }

}