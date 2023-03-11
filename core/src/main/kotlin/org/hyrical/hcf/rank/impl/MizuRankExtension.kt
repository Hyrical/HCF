package org.hyrical.hcf.rank.impl

import com.broustudio.AtomAPI.AtomAPI
import com.broustudio.MizuAPI.MizuAPI
import ltd.matrixstudios.alchemist.Alchemist
import ltd.matrixstudios.alchemist.api.AlchemistAPI
import ltd.matrixstudios.alchemist.service.profiles.ProfileGameService
import org.bukkit.entity.Player
import org.hyrical.hcf.rank.RankExtension

class MizuRankExtension : RankExtension {
    override fun getRankDisplay(player: Player): String {
        return MizuAPI.playerDataManager.getRankPrefix(MizuAPI.playerDataManager.getRank(player.uniqueId))
    }

    override fun getTagPrefix(player: Player): String? {
        return MizuAPI.playerDataManager.getTagDisplay(MizuAPI.playerDataManager.getTag(player.uniqueId))
    }
}