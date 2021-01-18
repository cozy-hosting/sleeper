package cozy.identity.requests

import com.trendyol.kediatr.Command
import cozy.identity.data.SigningRequest

class SigningRequestServiceApproveCommand(val signingRequest: SigningRequest): Command