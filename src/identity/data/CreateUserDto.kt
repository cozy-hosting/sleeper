package cozy.identity.data

import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate

data class CreateUserDto(
    val id: String,
    val name: String,
    val groups: Array<String>,
) {

    init {
        validate(this) {
            validate(CreateUserDto::id).isNotBlank()
            validate(CreateUserDto::name).isNotBlank()
            validate(CreateUserDto::groups).isNotEmpty()
        }
    }

}
