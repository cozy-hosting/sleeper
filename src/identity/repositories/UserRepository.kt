package cozy.identity.repositories

import cozy.services.cluster.data.ClusterUser

interface UserRepository {

    suspend fun retrieve(userIdentity: ClusterUser): ClusterUser

    suspend fun create(userIdentity: ClusterUser): ClusterUser

    suspend fun delete(userIdentity: ClusterUser): Boolean

}