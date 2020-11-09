package cozy.repositories.namespace

import io.fabric8.kubernetes.api.model.Namespace

interface NamespaceRepository {

    suspend fun retrieveAll(): List<Namespace>

    suspend fun retrieve(name: String): Namespace

    suspend fun create(namespace: Namespace): Namespace

    suspend fun delete(namespace: Namespace): Boolean

}