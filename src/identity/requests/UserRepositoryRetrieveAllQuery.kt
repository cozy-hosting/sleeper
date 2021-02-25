package cozy.identity.requests

import com.trendyol.kediatr.Query
import cozy.services.cluster.data.ClusterUser

class UserRepositoryRetrieveAllQuery(val drop: Int = 0, val take: Int = 10): Query<List<ClusterUser>>