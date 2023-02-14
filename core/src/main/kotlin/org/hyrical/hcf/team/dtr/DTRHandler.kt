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

        for (team in TeamManager.getTeams()){
            if (team.isRegenerating){
                RegenTask(HCFPlugin.instance, team)
            }
        }
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
            if (team.isRegenerating) continue

            if (team.dtr >= team.getMaxDTR()){
                teamsRegenerating.remove(team)
                team.dtr = team.getMaxDTR()
                continue
            }

            teamsRegenerating.remove(team)
            RegenTask(HCFPlugin.instance, team)
        }
    }

    fun startDTRTimer(team: Team){
        this.teamsRegenerating[team] = System.currentTimeMillis() + HCFPlugin.instance.config.getInt("TEAM-DTR.REGEN") * 1000 * 60
        team.isRegenerating = false
        team.save()
    }

    class RegenTask(val plugin: HCFPlugin, val team: Team) : BukkitRunnable() {
        init {
            runTaskTimer(plugin, 0L, 1200L)
        }

        override fun run() {
            if (TeamManager.getTeam(team.identifier) == null){
                this.cancel()
                return
            }

            if (hasTimer(team)){
                this.cancel()
                team.isRegenerating = false
                team.save()
                return
            }

            if (team.dtr >= team.getMaxDTR()){
                this.cancel()

                team.isRegenerating = false
                team.save()

                teamsRegenerating.remove(team)

                team.dtr = team.getMaxDTR()

                team.sendTeamMessage(LangFile.getString("TEAM.FINISHED-REGENERATING")!!)
                return
            }

            team.isRegenerating = true
            team.dtr = team.dtr + HCFPlugin.instance.config.getDouble("TEAM-DTR.REGEN-PER-MIN")
            team.save()

            team.sendTeamMessage(LangFile.getString("TEAM.REGENERATING")!!)
        }
    }
}