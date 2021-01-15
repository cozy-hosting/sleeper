package cozy.auth.services

import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemWriter
import java.io.StringWriter
import java.security.PrivateKey

fun PrivateKey.toPemString(): String {
    val stringWriter = StringWriter()
    val pemWriter = PemWriter(stringWriter)

    val pemSubject = "RSA PRIVATE KEY"
    val pemObject = PemObject(pemSubject, this.encoded)
    pemWriter.writeObject(pemObject)

    pemWriter.flush()
    pemWriter.close()

    return stringWriter.toString()
}

fun PKCS10CertificationRequest.toPemString(): String {
    val stringWriter = StringWriter()
    val pemWriter = PemWriter(stringWriter)

    val pemSubject = "CERTIFICATE REQUEST"
    val pemObject = PemObject(pemSubject, this.encoded)
    pemWriter.writeObject(pemObject)

    pemWriter.flush()
    pemWriter.close()

    return stringWriter.toString()
}