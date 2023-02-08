package org.hyrical.hcf.team.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Optional
import mkremins.fanciful.FancyMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamService
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate
import java.util.*
import kotlin.math.min


@CommandAlias("team|t|faction|f")
object TeamCommand : BaseCommand() {

    @HelpCommand
    fun help(player: Player){
        for (line in LangFile.getStringList("TEAM.TEAM-HELP")){
            player.sendMessage(translate(line))
        }
    }

    @CommandAlias("list")
    fun list(player: Player, @Optional amountOptional: Int?){


        object : BukkitRunnable(){
            override fun run() {
                val msg = FancyMessage()

                val page = amountOptional ?: 1
                val teamPlayerCount: HashMap<Team, Int> = HashMap()

                for (plr in Bukkit.getOnlinePlayers()) {
                    val playerTeam: Team? = plr.getProfile()!!.team

                    if (playerTeam != null) {
                        if (teamPlayerCount.containsKey(playerTeam)) {
                            teamPlayerCount[playerTeam] = teamPlayerCount[playerTeam]!! + 1
                        } else {
                            teamPlayerCount[playerTeam] = 1
                        }
                    }
                }

                val maxPages = teamPlayerCount.size / 10 + 1
                val currentPage = min(page, maxPages)

                val start: Int = (currentPage - 1) * 10
                var index = 0

                var teamList: String = ""

                val sortedTeamPlayerCount = sortByValues(teamPlayerCount)

                for (teamEntry in sortedTeamPlayerCount.entries) {
                    index++
                    if (index < start) {
                        continue
                    }
                    if (index > start + 10) {
                        break
                    }

                    val team = teamEntry.key

                    teamList +=
                }

                for (list in LangFile.getStringList("TEAM.TEAM-LIST.SHOWN_LIST")){
                    msg.text(translate(list.replace(
                        "%team_list%", "${TeamService.getTeams().forEach {  }}"
                    ))).then()
                }

                for (lore in LangFile.getStringList("TEAM.TEAM-LIST.HOVER-MESSAGE")){
                    msg.tooltip(translate(lore)).then()
                }

                msg.send(player)
            }
        }.runTaskAsynchronously(HCFPlugin.instance)

    }


    fun sortByValues(map: Map<Team, Int>): LinkedHashMap<Team, Int> {
        val list: LinkedList<Map.Entry<Team, Int>> = LinkedList<Map.Entry<Team, Int>>(map.entries)
        list.sortWith { (_, value): Map.Entry<Team, Int>, (_, value1): Map.Entry<Team, Int> ->
            value1.compareTo(
                value
            )
        }
        val sortedHashMap: LinkedHashMap<Team, Int> = LinkedHashMap()
        for ((key, value) in list) {
            sortedHashMap[key] = value
        }
        return sortedHashMap
    }

}
