package org.hyrical.hcf.profile.impl

import org.hyrical.hcf.profile.Profile
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.storage.StorageService

class JSONProfileService : ProfileService(StorageService.getRepository<Profile>("PROFILES").repository) {
    override fun byUsername(username: String): Profile? {
        return repository.findAll().firstOrNull { it.name == username }
    }
}