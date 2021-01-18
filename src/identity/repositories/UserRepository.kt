package cozy.identity.repositories

import cozy.services.cluster.data.ClusterUser

interface UserRepository {

    suspend fun retrieve(id: String): ClusterUser

    suspend fun create(userIdentity: ClusterUser): ClusterUser

    suspend fun delete(userIdentity: ClusterUser): Boolean

}