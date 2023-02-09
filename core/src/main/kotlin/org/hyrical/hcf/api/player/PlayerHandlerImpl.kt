package org.hyrical.hcf.api.player

import org.hyrical.hcf.player.PlayerHandler
import org.hyrical.hcf.player.Profile
import org.hyrical.hcf.profile.ProfileService
import java.util.*

class PlayerHandlerImpl : PlayerHandler() {
    override fun all(): List<Profile> {
        return ProfileService.all().map { mapOldToNew(it) }
    }

    override fun getProfile(name: String): Profile? {
        return ProfileService.getProfile(name)?.let { mapOldToNew(it) }
    }

    override fun getProfile(uuid: UUID): Profile? {
        return ProfileService.getProfile(uuid)?.let { mapOldToNew(it) }
    }

    private fun mapOldToNew(profile: org.hyrical.hcf.profile.Profile): Profile {
        return Profile(
            profile.identifier,
            profile.name,
            profile.kills,
            profile.deaths,
            profile.killstreak,
            profile.highestKillstreak,
            profile.balance,
            profile.coalMined,
            profile.ironMined,
            profile.goldMined,
            profile.diamondMined,
            profile.emeraldMined,
            profile.lapisMined,
            profile.redstoneMined,
            profile.joinedAt,
            profile.soulboundLives,
            profile.friendLives,
            profile.reclaimed,
            profile.playtime
        )
    }
}