package org.hyrical.hcf.provider.scoreboard.adapter.impl

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.classes.ArmorClassHandler
import org.hyrical.hcf.config.impl.ScoreboardFile
import org.hyrical.hcf.profile.ProfileService
import org.hyrical.hcf.provider.scoreboard.adapter.ScoreboardAdapter
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.staff.StaffModeManager
import org.hyrical.hcf.timer.type.PlayerTimer
import org.hyrical.hcf.timer.type.impl.playertimers.*
import org.hyrical.hcf.timer.type.impl.servertimers.SOTWTimer
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate
import java.util.LinkedList

class HCFScoreboardAdapter : ScoreboardAdapter {
    override fun getTitle(player: Player): String {
        return ScoreboardFile.getString("TITLE")!!
    }

    override fun getLines(player: Player): LinkedList<String> {
        val lines: LinkedList<String> = LinkedList()

        val currentHook = StaffModeManager.activeHook
        val combatTimer = CombatTimer.getRemainingTime(player)
        val enderPearlTimer = EnderpearlTimer.getRemainingTime(player)
        val appleTimer = AppleTimer.getRemainingTime(player)
        val logoutTimer = LogoutTimer.getRemainingTime(player)
        val abilityTimer = GlobalAbilityTimer.getRemainingTime(player)

        val profile = HCFPlugin.instance.profileService.getProfile(player.uniqueId)!!
        val team = profile.team

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

        if (currentHook.isModModed(player)) {
            val modlines = ScoreboardFile.getStringList("STAFFMODE.LINES")

            for (l in modlines) {
                val replace = l
                    .replace("%vanish%", if (currentHook.isVanished(player)) "&aYes" else "&cNo")
                    .replace("%online_players%", Bukkit.getOnlinePlayers().size.toString())
                lines.add(replace)
            }
        }

        if (ArmorClassHandler.getCurrentClass(player) != null) {
            lines.add(ScoreboardFile.getString("CLASSES.CURRENT-CLASS")!!
                .replace("%colored_class%", ArmorClassHandler.getPrettyClassName(ArmorClassHandler.getCurrentClass(player)!!)))
        }

        if (SOTWTimer.isSOTWActive()){
            if (SOTWTimer.isSOTWEnabled(player)){
                lines.add(ScoreboardFile.getString("SOTW.PROTECTION-ENABLED")!!.replace("%time%", TimeUtils.formatIntoFancy(SOTWTimer.timeRemaining - System.currentTimeMillis())))
            } else {
                lines.add(ScoreboardFile.getString("SOTW.PROTECTION-TIMER")!!.replace("%time%", TimeUtils.formatIntoFancy(SOTWTimer.timeRemaining - System.currentTimeMillis())))
            }
        }

        if (combatTimer != null){
            lines.add(ScoreboardFile.getString(CombatTimer.getConfigPath())!!.replace("%time%",
                TimeUtils.formatIntoMMSS((combatTimer / 1000).toInt())))
        }

        if (enderPearlTimer != null){
            lines.add(ScoreboardFile.getString(EnderpearlTimer.getConfigPath())!!.replace("%time%",
                TimeUtils.formatIntoFancy(enderPearlTimer)))
        }

        if (logoutTimer != null){
            lines.add(ScoreboardFile.getString(LogoutTimer.getConfigPath())!!.replace("%time%",
                TimeUtils.formatIntoFancy(logoutTimer)))
        }

        if (appleTimer != null){
            lines.add(ScoreboardFile.getString(AppleTimer.getConfigPath())!!.replace(
                    "%time%", TimeUtils.formatIntoFancy(appleTimer)))
        }

        if (abilityTimer != null){
            lines.add(ScoreboardFile.getString(GlobalAbilityTimer.getConfigPath())!!
                .replace("%time%", TimeUtils.formatIntoFancy(abilityTimer)))
        }

        if (team?.focusedTeam != null){
            val focusedTeam = team.focusedTeam!!
            for (line in ScoreboardFile.getStringList("FOCUS.LINES")){
                val newLine = line.replace("%team%", focusedTeam.name)
                    .replace("%hq%", focusedTeam.getFormattedHQ())
                    .replace("%dtr%", focusedTeam.getFormattedDTR())
                    .replace("%online%", focusedTeam.getOnlineMembers().size.toString())
                    .replace("%max%", focusedTeam.members.size.toString())

                if (!lines.isEmpty()){
                    lines.add(ScoreboardFile.getString("FOCUS.LINE-SEP")!!)
                }

                lines.add(newLine)
            }
        }

        if (!lines.isEmpty()){
            lines.addFirst(ScoreboardFile.getString("LINES")!!)
            lines.add(ScoreboardFile.getString("LINES")!!)
        }

        return lines.map { translate(it) }.toCollection(LinkedList())
    }
}