package cozy.identity.services

import cozy.identity.data.SigningRequest

interface SigningRequestService {

    suspend fun approve(signingRequest: SigningRequest): Boolean

}