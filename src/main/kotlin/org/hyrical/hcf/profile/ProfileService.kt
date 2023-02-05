package org.hyrical.hcf.profile

import com.eatthepath.uuid.FastUUID
import com.google.common.collect.HashBasedTable
import com.mongodb.client.model.Filters
import org.hyrical.hcf.storage.StorageService
import org.hyrical.store.DataStoreController
import org.hyrical.store.repository.impl.mongodb.MongoRepository
import org.hyrical.store.serializers.Serializers
import org.hyrical.store.type.StorageType
import java.util.*

object ProfileService {

    private val controller by lazy {
        DataStoreController.of<Profile>(
            StorageType.MONGO,
            StorageService.connection
        )
    }

    private val cache: HashBasedTable<UUID, String, Profile> = HashBasedTable.create()

    fun isCached(uuid: UUID): Boolean {
        return cache.containsRow(uuid)
    }

    fun getProfile(uuid: UUID): Profile? {
        // TODO: Unsure if this will work since I provided the column as null
        return cache.row(uuid).values.firstOrNull() ?: controller.repository.search(FastUUID.toString(uuid)).apply {
            performCacheAction(this)
        }

    }

    fun getProfile(name: String): Profile? {
        return cache.column(name).values.firstOrNull()  ?: Serializers.activeSerializer.deserialize(
            (controller.repository as MongoRepository<Profile>).collection.find(Filters.eq("name", name))
                .first()
                ?.toJson(), Profile::class.java
        ).apply {
            performCacheAction(this)
        }

    }

    fun getProfile(uuid: UUID, name: String): Profile? {
        return cache.get(uuid, name) ?: controller.repository.search(FastUUID.toString(uuid)).apply {
            performCacheAction(this)
        }
    }

    fun save(profile: Profile) {
        controller.repository.save(profile)
    }

    fun cacheProfile(profile: Profile) {
        cache.put(FastUUID.parseUUID(profile.identifier), profile.name, profile)
    }

    private fun performCacheAction(profile: Profile?) {
        if (profile != null) {
            cacheProfile(profile)
        }
    }
}