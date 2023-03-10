package org.hyrical.hcf.provider.scoreboard.adapter.impl

import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.ScoreboardFile
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.provider.scoreboard.adapter.ScoreboardAdapter
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.timer.type.PlayerTimer
import org.hyrical.hcf.timer.type.impl.playertimers.AppleTimer
import org.hyrical.hcf.timer.type.impl.playertimers.CombatTimer
import org.hyrical.hcf.timer.type.impl.playertimers.EnderpearlTimer
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate
import java.util.LinkedList

class HCFScoreboardAdapter : ScoreboardAdapter {
    override fun getTitle(player: Player): String {
        return ScoreboardFile.getString("TITLE")!!
    }

    override fun getLines(player: Player): LinkedList<String> {
        val lines: LinkedList<String> = LinkedList()

        val combatTimer = CombatTimer.getRemainingTime(player)
        val enderPearlTimer = EnderpearlTimer.getRemainingTime(player)
        val appleTimer = AppleTimer.getRemainingTime(player)

        val profile = HCFPlugin.instance.profileService.getProfile(player.uniqueId)!!

        if (ServerHandler.isKitMap){
            val kills = ScoreboardFile.getString("KITS.KILLS")!!
            val deaths = ScoreboardFile.getString("KITS.DEATHS")!!

            if (kills != ""){
                lines.add(kills.replace("%kills%", profile.kills.toString()))
            }

            if (deaths != ""){
                lines.add(deaths.replace("%deaths%", profile.deaths.toString()))
            }

            if (!lines.isEmpty()){
                lines.add("")
            }

        }

        if (combatTimer != null){
            lines.add(ScoreboardFile.getString(CombatTimer.getConfigPath())!!.replace("%time%",
                TimeUtils.formatIntoMMSS((combatTimer / 1000).toInt())))
        }

        if (enderPearlTimer != null){
            lines.add(ScoreboardFile.getString(EnderpearlTimer.getConfigPath())!!.replace("%time%",
                TimeUtils.formatFancy(enderPearlTimer / 1000L)))
        }

        if (appleTimer != null){
            lines.add(ScoreboardFile.getString(AppleTimer.getConfigPath())!!.replace("" +
                    "%time%", TimeUtils.formatFancy(appleTimer / 1000L)))
        }

        if (!lines.isEmpty()){
            lines.addFirst(ScoreboardFile.getString("LINES")!!)
            lines.add(ScoreboardFile.getString("LINES")!!)
        }

        return lines.map { translate(it) }.toCollection(LinkedList())
    }
}