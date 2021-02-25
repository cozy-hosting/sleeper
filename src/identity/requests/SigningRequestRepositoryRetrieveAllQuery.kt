package cozy.identity.requests

import com.trendyol.kediatr.Query
import cozy.identity.data.SigningRequest

class SigningRequestRepositoryRetrieveAllQuery(val drop: Int = 0, val take: Int = 10): Query<List<SigningRequest>>