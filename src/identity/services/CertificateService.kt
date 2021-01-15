package cozy.auth.services

import org.bouncycastle.pkcs.PKCS10CertificationRequest
import java.security.KeyPair
import javax.security.auth.x500.X500Principal

interface CertificateService {

    suspend fun generateKeyPair(): KeyPair

    suspend fun buildSigningRequest(keyPair: KeyPair, principal: X500Principal): PKCS10CertificationRequest

}