package org.hyrical.hcf.team.dtr

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager

object DTRHandler : Runnable {

    private val teamsRegenerating: HashMap<Team, Long> = hashMapOf()

    fun load(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(HCFPlugin.instance, this, 20L, 20L)

        HCFPlugin.instance.logger.info("[DTR Handler] Loaded successfully.")
    }

    fun getRemaining(team: Team): Long {
        return if (teamsRegenerating.containsKey(team)) teamsRegenerating[team]!! - System.currentTimeMillis() else 0L
    }

    fun hasTimer(team: Team): Boolean {
        return getRemaining(team) > 0L
    }

    override fun run() {
        for (team in TeamManager.getTeams()){
            if (TeamManager.getTeam(team.identifier) == null){
                teamsRegenerating.remove(team)
                continue
            }
            if (hasTimer(team)) continue

            teamsRegenerating.remove(team)
            RegenTask(HCFPlugin.instance, team)
        }
    }

    fun startDTRTimer(team: Team){
        this.teamsRegenerating[team] = System.currentTimeMillis() + HCFPlugin.instance.config.getInt("TEAM-DTR.REGEN") * 60 * 20
        team.isRegening = false
        team.save()
    }

    class RegenTask(val plugin: HCFPlugin, val team: Team) : BukkitRunnable() {
        init {
            runTaskTimerAsynchronously(plugin, 0L, 1200L)
        }

        override fun run() {
            if (TeamManager.getTeam(team.identifier) == null){
                this.cancel()
                return
            }

            if (hasTimer(team)){
                this.cancel()
                team.isRegening = false
                team.save()
                return
            }

            team.isRegening = true
            team.dtr = team.dtr + HCFPlugin.instance.config.getInt("TEAM-DTR.REGEN-PER-MIN")
            team.save()

            team.sendTeamMessage(LangFile.getString("TEAM.REGENERATING")!!)

            if (team.dtr >= team.getMaxDTR()){
                this.cancel()

                team.isRegening = false
                team.save()

                team.sendTeamMessage(LangFile.getString("TEAM.FINISHED-REGENERATING")!!)
            }
        }
    }
}