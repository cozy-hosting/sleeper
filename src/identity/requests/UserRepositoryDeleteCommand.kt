package cozy.identity.requests

import com.trendyol.kediatr.Command
import cozy.services.cluster.data.ClusterUser

class UserRepositoryDeleteCommand(val userIdentity: ClusterUser): Command