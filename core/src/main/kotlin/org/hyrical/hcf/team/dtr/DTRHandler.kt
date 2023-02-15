package org.hyrical.hcf.team.dtr

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import java.util.concurrent.TimeUnit

object DTRHandler : BukkitRunnable() {

    private val teamsRegenerating: HashMap<String, Long> = hashMapOf()
    private val teamLastRegen: HashMap<String, Long> = hashMapOf()

    fun load(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(HCFPlugin.instance, this, 20L, 20L)

        HCFPlugin.instance.logger.info("[DTR Handler] Loaded successfully.")
    }

    fun getRemaining(team: Team): Long {
        return if (teamsRegenerating.containsKey(team.identifier)) teamsRegenerating[team.identifier]!! - System.currentTimeMillis() else 0L
    }

    fun hasTimer(team: Team): Boolean {
        return getRemaining(team) > 0L
    }

    override fun run() {
        for (team in TeamManager.getTeams()){
            if (TeamManager.getTeam(team.identifier) == null){
                teamsRegenerating.remove(team.identifier)
                continue
            }

            if (hasTimer(team)) continue
            if (team.isRegenerating) continue

            if (team.dtr >= team.getMaxDTR()){
                teamsRegenerating.remove(team.identifier)
                team.dtr = team.getMaxDTR()
                continue
            }

            teamsRegenerating.remove(team.identifier)

            if (hasTimer(team)){
                this.cancel()
                team.isRegenerating = false
                team.save()
                continue
            }

            if (teamLastRegen.containsKey(team.identifier) && teamLastRegen[team.identifier]!! < System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1)) continue

            if (team.dtr >= team.getMaxDTR()){
                this.cancel()

                team.isRegenerating = false
                team.save()

                teamsRegenerating.remove(team.identifier)
                teamLastRegen.remove(team.identifier)

                team.dtr = team.getMaxDTR()

                team.sendTeamMessage(LangFile.getString("TEAM.FINISHED-REGENERATING")!!)
                continue
            }

            teamLastRegen[team.identifier] = System.currentTimeMillis()

            team.isRegenerating = true
            team.dtr = team.dtr + HCFPlugin.instance.config.getDouble("TEAM-DTR.REGEN-PER-MIN")
            team.save()

            team.sendTeamMessage(LangFile.getString("TEAM.REGENERATING")!!)
        }
    }

    fun startDTRTimer(team: Team){
        this.teamsRegenerating[team.identifier] = System.currentTimeMillis() + HCFPlugin.instance.config.getInt("TEAM-DTR.REGEN") * 1000 * 60
        team.isRegenerating = false
        team.save()
    }
}