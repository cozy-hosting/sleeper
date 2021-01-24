package cozy.identity.repositories

import cozy.services.cluster.data.ClusterUser

interface UserRepository {

    suspend fun retrieveAll(drop: Int = 0, skip: Int = 10): List<ClusterUser>

    suspend fun retrieve(id: String): ClusterUser

    suspend fun create(userIdentity: ClusterUser): ClusterUser

    suspend fun delete(userIdentity: ClusterUser): Boolean

}