package org.hyrical.hcf.profile.impl

import com.mongodb.client.model.Filters
import org.hyrical.hcf.profile.Profile
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.storage.StorageService
import org.hyrical.store.repository.impl.mongodb.MongoRepository
import org.hyrical.store.serializers.Serializers

class MongoDBProfileService : ProfileService(StorageService.getRepository<Profile>("PROFILES").repository) {
    override fun byUsername(username: String): Profile? {
        return Serializers.activeSerializer.deserialize(
            (repository as MongoRepository<Profile>).collection.find(Filters.eq("name", username))
                .first()
                ?.toJson(), Profile::class.java
        )
    }
}