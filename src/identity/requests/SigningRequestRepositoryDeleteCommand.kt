package cozy.identity.requests

import com.trendyol.kediatr.Command
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest

class SigningRequestRepositoryDeleteCommand(val certificateSigningRequest: CertificateSigningRequest): Command