package org.hyrical.hcf.rank.impl

import com.broustudio.AtomAPI.AtomAPI
import ltd.matrixstudios.alchemist.Alchemist
import ltd.matrixstudios.alchemist.api.AlchemistAPI
import ltd.matrixstudios.alchemist.service.profiles.ProfileGameService
import org.bukkit.entity.Player
import org.hyrical.hcf.rank.RankExtension

class AtomRankExtension : RankExtension {
    override fun getRankDisplay(player: Player): String {
        return AtomAPI.getInstance().rankManager.getRankPrefix(player.uniqueId)
    }

    override fun getTagPrefix(player: Player): String? {
        return null
    }
}