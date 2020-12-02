package cozy.services.cert

import cozy.repositories.cert.data.SigningRequest

interface SigningRequestService {

    suspend fun approve(signingRequest: SigningRequest): Boolean

}