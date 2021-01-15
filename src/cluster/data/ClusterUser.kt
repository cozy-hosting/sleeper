package cozy.services.cluster.data

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x500.style.IETFUtils
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.util.io.pem.PemReader
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate
import java.io.StringReader
import javax.security.auth.x500.X500Principal

data class ClusterUser(
    val id: String,
    val name: String,
    val groups: Array<String>,
    val clientCert: String? = null,
    val clientKey: String? = null
) {

    companion object {
        private fun getSubject(clientCert: String): X500Name {
            val reader = StringReader(clientCert)
            val pemReader = PemReader(reader)

            val pemObject = pemReader.readPemObject()
            return X509CertificateHolder(pemObject.content).subject
        }

        private fun getId(clientCert: String): String {
            val subject = getSubject(clientCert)
            val serialNumber = subject.getRDNs(BCStyle.OU).first()

            return IETFUtils.valueToString(serialNumber.first.value)
        }

        private fun getName(clientCert: String): String {
            val subject = getSubject(clientCert)
            val commonName = subject.getRDNs(BCStyle.CN).first()

            return IETFUtils.valueToString(commonName.first.value)
        }

        private fun getGroups(clientCert: String): Array<String> {
            val subject = getSubject(clientCert)
            val organizations = subject.getRDNs(BCStyle.O)

            return organizations.map {
                IETFUtils.valueToString(it.first.value)
            }.toTypedArray()
        }
    }

    constructor(clientCert: String, clientKey: String): this(
        id = getId(clientCert),
        name = getName(clientCert),
        groups = getGroups(clientCert),
        clientCert,
        clientKey
    )

    init {
        validate(this) {
            validate(ClusterUser::id).isNotBlank()
            validate(ClusterUser::name).isNotBlank()
            validate(ClusterUser::groups).isNotEmpty()
        }
    }

    fun toPrincipal(): X500Principal {
        return X500Principal(toString())
    }

    override fun toString(): String {
        val groupsBuilder = StringBuilder()

        groups.forEach { groupsBuilder.append("O=${it},") }
        return "CN=${name},OU=${id},${groupsBuilder.dropLast(1)}"
    }

}