package cozy.identity.requests

import com.trendyol.kediatr.Query
import cozy.identity.data.SigningRequest

class SigningRequestRepositoryRetrieveQuery(val name: String): Query<SigningRequest?>