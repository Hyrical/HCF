package org.hyrical.hcf.provider.tab.impl

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.TabFile
import org.hyrical.hcf.provider.tab.Tab
import org.hyrical.hcf.provider.tab.TabAdapter
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.user.TeamUser
import org.hyrical.hcf.utils.DirectionUtils
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.plugin.PluginUtils
import org.hyrical.hcf.utils.translate
import java.text.NumberFormat
import java.util.stream.Collectors
import kotlin.math.round

class HCFTab : TabAdapter {
    private var farRightTablist: MutableList<String> = mutableListOf()
    private var leftTablist: MutableList<String> = mutableListOf()
    private var middleTablist: MutableList<String> = mutableListOf()
    private var rightTablist: MutableList<String> = mutableListOf()

    init {
        leftTablist = TabFile.getStringList("LEFT")
        middleTablist = TabFile.getStringList("MIDDLE")
        rightTablist = TabFile.getStringList("RIGHT")
        farRightTablist = TabFile.getStringList("FAR_RIGHT")
    }

    override fun getHeader(player: Player): String {
        return translate(TabFile.getString("HEADER")!!)
    }

    override fun getInfo(player: Player): Tab {
        val tablist: Tab = HCFPlugin.instance.tabHandler.tablists[player.uniqueId]!!
        val team = player.getProfile()!!.team

        val profile = player.getProfile()!!

        for (i in 0..19) {
            tablist.add(0, i, translate(leftTablist[i]))
            tablist.add(1, i, translate(middleTablist[i]))
            tablist.add(2, i, translate(rightTablist[i]))
            tablist.add(3, i, translate(farRightTablist[i]))
        }

        val teams = TeamManager.getTeams().stream().filter { it.getOnlineMembers().isNotEmpty() && it.leader != null}
            .sorted { t1, t2 -> t2.getOnlineMembers().size - t1.getOnlineMembers().size }
            .collect(Collectors.toList())


        for (entry in tablist.entries.values()) {
            var text: String = entry.text
            if (text.isEmpty()) {
                continue
            }
            if (team != null) {
                val teamFormat: List<String> = TabFile.getStringList("TEAM-INFO.HAS-TEAM")
                val teamMembers: List<TeamUser> = team.getOnlineMembers()
                teamMembers.sortedWith { x: TeamUser, y: TeamUser ->
                    y.role.ordinal - x.role.ordinal
                }

                text = text.replace("%team-name%", team.getFormattedTeamName(player))

                for (i in teamFormat.indices) {
                    val replacedText: String = teamFormat[i].replace("%dtr-color%", team.getDTRColor())
                        .replace("%dtr%", team.getDTRFormat().format(team.dtr))
                        .replace("%dtr-symbol%", team.getDTRSymbol())
                        .replace("%players%", team.getOnlineMembers().size.toString())
                        .replace("%max%", team.members.size.toString())
                        .replace("%balance%", NumberFormat.getInstance().format(team.balance))
                        .replace("%points%", team.calculatePoints().toString())
                        .replace("%home%", team.getFormattedHQ())
                        .replace("%team-name%", team.getFormattedTeamName(player))

                    text = text.replace("%teaminfo-$i%", replacedText)
                }

                for (i in teamMembers.indices) {
                    val member: TeamUser = teamMembers[i]
                    val player = Bukkit.getPlayer(member.uuid) ?: continue
                    text = text.replace("%member-$i%", "&7${team.getStar(player)}&a${player.name}")
                }
            } else {
                val noTeamFormat: List<String> = TabFile.getStringList("TEAM-INFO.NOT-SET")

                text = text.replace("%team-name%", "")
                for (i in noTeamFormat.indices) {
                    val tt = noTeamFormat[i]
                    text = text.replace("%teaminfo-$i%", tt)
                }
            }
            var i = 0


            while (i < teams.size && i != 19) {
                val targetTeam: Team = teams[i]

                val listFormat = TabFile.getString("TEAM-LIST-FORMAT")!!
                    .replace("%relation-color%", targetTeam.getRelationColor(player))
                    .replace("%name%", targetTeam.name)
                    .replace("%online%", targetTeam.getOnlineMembers().size.toString())
                    .replace("%max%", targetTeam.members.size.toString())
                    .replace("%dtr-color%", targetTeam.getDTRColor())
                    .replace("%dtr%", targetTeam.getDTRFormat().format(targetTeam.dtr))
                    .replace("%dtr-symbol%", targetTeam.getDTRSymbol())

                text = text.replace("%team-$i%", listFormat)
                ++i
            }
            if (text.contains("%team-") || text.contains("%teaminfo-") || text.contains("%member-")) {
                entry.text = ""
            } else {
                entry.text = text.replace("%kills%", profile.kills.toString())
                    .replace("%deaths%", profile.deaths.toString())
                    .replace("%balance%", NumberFormat.getInstance().format(profile.balance))
                    .replace("%current-killstreak%", profile.killstreak.toString())
                    .replace("%highest-killstreak%", profile.highestKillstreak.toString())
                    .replace("%claim%", "&aSpawn")
                    .replace("%x%", (round(player.location.x).toInt()).toString())
                    .replace("%z%", (round(player.location.z).toInt()).toString())
                    .replace("%facing%", DirectionUtils.getCardinalDirection(player)!!)
                    .replace("%online%", PluginUtils.getOnlinePlayers().size.toString())
                    .replace("%max%", Bukkit.getMaxPlayers().toString())
            }
            entry.text = translate(entry.text)
        }

        return tablist
    }

    override fun getFooter(player: Player): String {
        return translate(TabFile.getString("FOOTER")!!).replace("%players%", Bukkit.getOnlinePlayers().size.toString())

    }

    fun load(){
        for (i in 0..19) {
            val left: List<String> = this.leftTablist[i].split(";")
            this.leftTablist[i] = if (left.size == 1) "" else left[1]
            val middle: List<String> = this.middleTablist[i].split(";")
            this.middleTablist[i] = if (middle.size == 1) "" else middle[1]
            val right: List<String> = this.rightTablist[i].split(";")
            this.rightTablist[i] = if (right.size == 1) "" else right[1]
            val farRight: List<String> = this.farRightTablist[i].split(";")
            this.farRightTablist[i] = if (farRight.size == 1) "" else farRight[1]
        }
    }
}