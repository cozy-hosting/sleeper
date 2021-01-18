package cozy.identity.data

import kotlinx.serialization.Serializable
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate

@Serializable
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreateUserDto

        if (id != other.id) return false
        if (name != other.name) return false
        if (!groups.contentEquals(other.groups)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + groups.contentHashCode()
        return result
    }

}
