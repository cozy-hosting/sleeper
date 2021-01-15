package cozy.auth.services

import cozy.auth.repositories.data.SigningRequest

interface SigningRequestService {

    suspend fun approve(signingRequest: SigningRequest): Boolean

}