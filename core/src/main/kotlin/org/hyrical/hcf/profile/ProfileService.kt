package org.hyrical.hcf.profile

import com.eatthepath.uuid.FastUUID
import com.mongodb.client.model.Filters
import org.hyrical.hcf.storage.StorageService
import org.hyrical.hcf.utils.table.ExpirableHashBasedTable
import org.hyrical.store.DataStoreController
import org.hyrical.store.repository.Repository
import org.hyrical.store.repository.impl.mongodb.MongoRepository
import org.hyrical.store.serializers.Serializers
import org.hyrical.store.type.StorageType
import java.util.*

abstract class ProfileService(val repository: Repository<Profile>) {

    abstract fun byUsername(username: String): Profile?

    private val cache: ExpirableHashBasedTable<UUID, String, Profile> = ExpirableHashBasedTable(1000L * 60L * 5L)

    fun isCached(uuid: UUID): Boolean {
        return cache.containsRow(uuid)
    }

    fun getProfile(uuid: UUID): Profile? {
        return cache.row(uuid).values.firstOrNull() ?: repository.search(FastUUID.toString(uuid)).apply {
            performCacheAction(this)
        }
    }

    fun getProfile(name: String): Profile? {
        return cache.column(name).values.firstOrNull()  ?: byUsername(name).apply {
            performCacheAction(this)
        }
    }

    fun getProfile(uuid: UUID, name: String): Profile? {
        return cache.get(uuid, name) ?: repository.search(FastUUID.toString(uuid)).apply {
            performCacheAction(this)
        }
    }

    fun save(profile: Profile) {
        repository.save(profile)
    }

    fun cacheProfile(profile: Profile) {
        cache.put(FastUUID.parseUUID(profile.identifier), profile.name, profile)
    }

    fun all(): List<Profile> {
        return repository.findAll()
    }

    private fun performCacheAction(profile: Profile?) {
        if (profile != null) {
            cacheProfile(profile)
        }
    }
}