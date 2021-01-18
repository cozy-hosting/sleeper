package cozy.identity.requests

import com.trendyol.kediatr.Query
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import java.security.KeyPair
import javax.security.auth.x500.X500Principal

class CertificateServiceBuildQuery(val keyPair: KeyPair, val principal: X500Principal) :
    Query<PKCS10CertificationRequest>