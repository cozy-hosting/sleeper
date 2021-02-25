package identity.asserts

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import java.util.*

class PemFormatAssert(private val dataInPemFormat: String):
    AbstractAssert<PemFormatAssert, String>(dataInPemFormat, PemFormatAssert::class.java) {

   companion object {

       fun assertThat(dataInPemFormat: String): PemFormatAssert {
           return PemFormatAssert(dataInPemFormat)
       }

   }

    fun isInPemFormat(subject: String) {
        val pemPrefix = "-----BEGIN $subject-----"
        val pemSuffix = "-----END $subject-----\n"

        val pemValue = actual
            .removePrefix(pemPrefix)
            .removeSuffix(pemSuffix)
            .replace("\n", "")

        var decodedPemValue: ByteArray? = null
        try {
            decodedPemValue = Base64.getDecoder().decode(pemValue)
        } catch (_: IllegalArgumentException) {}

        Assertions.assertThat(dataInPemFormat).startsWith(pemPrefix)
        Assertions.assertThat(decodedPemValue).isNotNull
        Assertions.assertThat(dataInPemFormat).endsWith(pemSuffix)
    }

}
