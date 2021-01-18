package cozy.identity.requests

import com.trendyol.kediatr.Query
import cozy.services.cluster.data.ClusterUser

class UserRepositoryRetrieveQuery(val id: String): Query<ClusterUser>