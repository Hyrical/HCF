package org.hyrical.hcf.team.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import co.aikar.commands.annotation.Optional
import ltd.matrixstudios.alchemist.util.Chat
import mkremins.fanciful.FancyMessage
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.chat.mode.ChatMode
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.serialize.LocationSerializer
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.claim.LandGrid
import org.hyrical.hcf.team.claim.cuboid.Cuboid
import org.hyrical.hcf.team.claim.listener.ClaimListener
import org.hyrical.hcf.team.claim.logic.ClaimProcessor
import org.hyrical.hcf.team.commands.menu.SystemTeamMenu
import org.hyrical.hcf.team.dtr.DTRHandler
import org.hyrical.hcf.team.system.Flag
import org.hyrical.hcf.team.user.TeamRole
import org.hyrical.hcf.team.user.TeamUser
import org.hyrical.hcf.timer.type.impl.playertimers.CombatTimer
import org.hyrical.hcf.timer.type.impl.playertimers.HomeTimer
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate
import java.text.NumberFormat
import java.util.*


@CommandAlias("team|t|faction|f")
object TeamCommand : BaseCommand() {

    @HelpCommand
    fun help(player: Player) {
        for (line in LangFile.getStringList("TEAM.TEAM-HELP")) {
            player.sendMessage(translate(line))
        }
    }

    @Subcommand("editor")
    @CommandPermission("hcf.admin")
    fun editor(player: Player) {
        SystemTeamMenu().openMenu(player)
    }

    @Subcommand("who|i|info")
    fun who(player: Player, @Optional teamInput: Team?) {
        object : BukkitRunnable() {
            override fun run() {
                val profile = player.getProfile()!!

                if (teamInput == null) {
                    val team = profile.team

                    if (team == null) {
                        player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))
                        return
                    }
                }

                val team = teamInput ?: profile.team

                team!!.sendTeamInformation(player)
            }
        }.runTaskAsynchronously(HCFPlugin.instance)
    }

    @Subcommand("create")
    fun create(player: Player, @Name("name") name: String) {
        if (TeamManager.getTeam(name) != null) return player.sendMessage(translate(LangFile.getString("TEAM.TEAM-ALREADY-EXISTS")!!))

        if (!TeamManager.isValidTeamText(name)) {
            player.sendMessage(translate(LangFile.getString("TEAM.INVALID-NAME")!!))
            return
        }

        val user = TeamUser(player.uniqueId, TeamRole.LEADER)
        val team = Team(
            name.lowercase(), name, user,
            members = mutableListOf(user)
        )

        TeamManager.create(team)

        val profile = player.getProfile()!!

        profile.teamString = team.identifier
        profile.save()

        player.sendMessage(translate(LangFile.getString("TEAM.TEAM-CREATE-INFO")!!))
        Bukkit.broadcastMessage(
            translate(
                LangFile.getString("TEAM.TEAM-CREATE")!!
                    .replace("%name%", name).replace("%player%", player.displayName)
            )
        )

        HCFPlugin.instance.nametagHandler.update()
    }

    @Subcommand("disband")
    fun disband(player: Player) {
        if (player.getProfile()!!.teamString == null) return player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))

        val profile = player.getProfile()!!
        val team = profile.team!!

        if (!team.isLeader(player.uniqueId)) return player.sendMessage(
            translate(
                LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!
                    .replace("%role%", "Leader")
            )
        )

        team.disband()

        player.getProfile()!!.teamString = null
        player.getProfile()!!.save()

        team.sendTeamMessage(translate(LangFile.getString("TEAM.DISBAND-TEAM-MSG")!!))
        Bukkit.broadcastMessage(
            translate(
                LangFile.getString("TEAM.DISBAND")!!.replace("%name%", team.name).replace("%player%", player.name)
            )
        )

        HCFPlugin.instance.nametagHandler.update()
    }

    @Subcommand("teleport|tp")
    @CommandPermission("hcf.staff")
    fun teleport(player: Player, @Name("team")team: Team) {
        val hqLocation = team.hq ?: return player.sendMessage(translate("&c${team.name} does not have an HQ"))
        val toTeleport = LocationSerializer.deserialize(hqLocation)

        player.teleport(toTeleport)
    }

    @Subcommand("accept")
    fun accept(player: Player, @Name("team") team: Team) { // We have a param type, so we can use this.
        if (player.getProfile()!!.teamString != null) return player.sendMessage(
            translate(
                LangFile.getString("TEAM.ALREADY_IN_TEAM")!!.replace("%player%", player.name)
            )
        )

        if (!team.invitations.contains(player.uniqueId)) return player.sendMessage(translate(LangFile.getString("TEAM.NOT-INVITED")!!))
        if (!team.isLeader(player.uniqueId) || !team.isCoLeader(player.uniqueId) || !team.isCaptain(player.uniqueId)) return player.sendMessage(
            translate(LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!.replace("%role%", "Captain"))
        )
        if (team.members.size == ServerHandler.maxFactionSize) return player.sendMessage(
            translate(
                LangFile.getString("TEAM.CANNOT-FULL-FACTION")!!.replace("%team%", team.name)
            )
        )
        if (DTRHandler.hasTimer(team)) return player.sendMessage(
            translate(
                LangFile.getString("TEAM.CANNOT-JOIN-REGENERATION")!!.replace("%team%", team.name)
            )
        )
        if (CombatTimer.hasTimer(player)) return player.sendMessage(
            translate(
                LangFile.getString("TEAM.CANNOT-JOIN-COMBAT")!!.replace("%team%", team.name)
            )
        )

        team.invitations.remove(player.uniqueId)
        team.members.add(TeamUser(player.uniqueId, TeamRole.MEMBER))

        player.getProfile()!!.teamString = team.identifier
        player.getProfile()!!.save()

        team.sendTeamMessage(translate(LangFile.getString("TEAM.PLAYER-ACCEPTED")!!.replace("%player%", player.name)))

        HCFPlugin.instance.nametagHandler.update()
    }

    @Subcommand("leave")
    fun leave(player: Player) {
        val profile = player.getProfile()!!

        if (profile.teamString == null) return player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))

        val team = profile.team!!

        if (team.isLeader(player.uniqueId) && team.members.size > 1) return player.sendMessage(
            translate(
                LangFile.getString(
                    "TEAM.CHOOSE-NEW-LEADER"
                )!!
            )
        )
        if (CombatTimer.hasTimer(player)) return player.sendMessage(translate(LangFile.getString("COMBAT-TIMER.CANNOT-WHILE-COMBAT")!!))

        if (team.members.size > 1) {
            team.members.removeIf { it.uuid == player.uniqueId }
            profile.teamString = null

            team.sendTeamMessage(LangFile.getString("TEAM.LEFT-TEAM-SEND")!!.replace("%player%", player.name))
            player.sendMessage(translate(LangFile.getString("TEAM.LEFT-TEAM")!!))

            profile.save()
        } else {
            team.disband()

            player.sendMessage(translate(LangFile.getString("TEAM.LEFT-TEAM-DISBANDED")!!))
        }

        HCFPlugin.instance.nametagHandler.update()
    }

    @Subcommand("invite|inv")
    fun invite(player: Player, @Name("player") target: OfflinePlayer) {
        if (player.getProfile()!!.teamString == null) return player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))

        val targetProfile = target.getProfile()!!
        val playerTeam = player.getProfile()!!.team!!

        if (playerTeam.members.size >= ServerHandler.maxFactionSize) {
            player.sendMessage(
                translate(
                    LangFile.getString("TEAM.MAX-FACTION-SIZE")!!
                        .replace("%maxSize%", ServerHandler.maxFactionSize.toString())
                )
            )
            return
        }

        if (!playerTeam.isLeader(player.uniqueId) || !playerTeam.isCoLeader(player.uniqueId) || !playerTeam.isCaptain(
                player.uniqueId
            )
        ) {
            return player.sendMessage(
                translate(
                    LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!
                        .replace("%role%", "Captain")
                )
            )
        }

        if (playerTeam.isMember(player.uniqueId)) {
            return player.sendMessage(
                translate(
                    LangFile.getString("TEAM.ALREADY-IN-TEAM")!!.replace("%player%", target.name!!)
                )
            )
        }

        if (targetProfile.invitations.contains(playerTeam.identifier)) {
            return player.sendMessage(translate(LangFile.getString("TEAM.ALREADY-INVITED")!!))
        }

        targetProfile.invitations.add(playerTeam.identifier)
        targetProfile.save()

        val bukkitTarget = Bukkit.getPlayer(target.uniqueId)

        if (bukkitTarget != null) {
            val message = FancyMessage(
                LangFile.getString("TEAM.TEAM-INVITED.MESSAGE")!!
                    .replace("\\n", "\n").replace("%team%", playerTeam.name)
            )

            message.tooltip(translate(LangFile.getString("TEAM.TEAM-INVITED.TOOLTIP")!!))
            message.command(translate(LangFile.getString("TEAM.TEAM-INVITED.COMMAND")!!))

            message.send(bukkitTarget)
        }
    }

    @CommandAlias("claim")
    fun claim(player: Player) {
        val team = player.getProfile()?.team

        if (team == null) {
            player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))
            return
        }

        if (!team.isAtLeast(player.uniqueId, TeamRole.COLEADER)) {
            return player.sendMessage(
                translate(
                    LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!
                        .replace("%role%", "Co-Leader")
                )
            )
        }

        player.inventory.addItem(ClaimListener.claimWand)
        player.updateInventory()
        player.sendMessage(translate("&eYou have received a claim wand"))

        LandGrid.pendingSession[player.uniqueId] = ClaimProcessor(null, null)
    }

    @CommandAlias("sethome")
    fun sethome(player: Player) {
        val team = player.getProfile()?.team

        if (team == null) {
            player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))
            return
        }

        if (!team.isAtLeast(player.uniqueId, TeamRole.COLEADER)) {
            return player.sendMessage(
                translate(
                    LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!
                        .replace("%role%", "Co-Leader")
                )
            )
        }

        val loc = player.location

        if (team.claims.none { it.contains(loc)}) {
            return player.sendMessage(translate(LangFile.getString("TEAM_HOME_NOT_IN_CLAIM")!!))
        }

        team.hq = LocationSerializer.serialize(player.location)
        TeamManager.save(team)
        player.sendMessage(translate(LangFile.getString("TEAM.SET_TEAM_HOME")!!))
    }

    @CommandAlias("home|hq")
    fun hq(player: Player){
        val profile = player.getProfile()!!

        if (profile.teamString == null) return player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))

        val team = profile.team!!

        if (team.hq == null){
            player.sendMessage(translate(LangFile.getString("TEAM.NO_HQ_SET")!!))
            return
        }

        if (player.gameMode != GameMode.CREATIVE) {

            if (ServerHandler.eotw) {
                player.sendMessage(translate(LangFile.getString("TEAM.CANNOT_TELEPORT_EOTW")!!))
                return
            }

            if (profile.pvpTimer > System.currentTimeMillis()) {
                player.sendMessage(translate(LangFile.getString("TEAM.CANNOT_TELEPORT_PVPTIMER")!!))
                return
            }

            HomeTimer.applyTimer(player)

            player.sendMessage(
                translate(
                    LangFile.getString("HOME.STARTED")!!
                        .replace("%time%", HCFPlugin.instance.config.getInt("TIMERS.HOME.TIME").toString())
                )
            )
        } else {
            val deserialized = LocationSerializer.deserialize(team.hq!!)
            player.teleport(deserialized)

            player.sendMessage(translate(LangFile.getString("HOME.WARPED")!!
                .replace("%team%", team.name)))
        }
    }

    @CommandAlias("unclaim")
    fun unclaim(player: Player) {
        val profile = player.getProfile()!!

        if (profile.teamString == null) return player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))

        val team = profile.team!!

        if (!team.isAtLeast(player.uniqueId, TeamRole.COLEADER)) {
            return player.sendMessage(
                translate(
                    LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!
                        .replace("%role%", "Co-Leader")
                )
            )
        }

        team.claims.clear()
        TeamManager.save(team)
        player.sendMessage(translate(LangFile.getString("TEAM.UNCLAIMED_ALL_LAND")!!))
    }

    @CommandAlias("c|chat")
    fun chat(player: Player, @Optional chatMode: String?) {
        val profile = player.getProfile()!!

        if (chatMode == null) {
            profile.chatMode = ChatMode.values()[if (profile.chatMode.ordinal == 4) 0 else profile.chatMode.ordinal + 1]
        } else {
            when (chatMode) {
                "p", "public" -> profile.chatMode = ChatMode.PUBLIC
                "f", "faction" -> profile.chatMode = ChatMode.TEAM
                "o", "officer", "c", "captain" -> profile.chatMode = ChatMode.OFFICER
                "l", "leader" -> profile.chatMode = ChatMode.LEADER
                "a", "ally" -> profile.chatMode = ChatMode.ALLY
            }
        }

        profile.save()

        player.sendMessage(
            translate(
                LangFile.getString("TEAM.TEAM-CHAT.NOW-TALKING")!!.replace("%chat%", profile.chatMode.displayName)
            )
        )
    }

    @Subcommand("flag")
    @CommandPermission("hcf.admin")
    fun flag(player: Player, @Name("flag") thing: String) {
        val team = player.getProfile()!!.team!!

        team.flags.add(Flag.valueOf(thing))
        team.save()
    }

    @Subcommand("forceleave")
    @CommandPermission("hcf.admin")
    fun forceleave(player: Player) {
        val profile = player.getProfile()!!
        val team = profile.team!!

        team.leader = null
        team.save()

        profile.teamString = null
        profile.save()
    }

    @Subcommand("pointbreakdown")
    fun pointBreakdown(player: Player, @Name("target") team: Team) {
        val config = HCFPlugin.instance.config

        /*
            Calculating points per kill

            Total Points = multi * value
            Multiplier = total/value
            Raw Points = total/multi
         */
        val kmp = config.getInt("POINTS.POINTS-KILL")
        val dmp = config.getInt("POINTS.POINTS-DEATH")
        val ktmp = config.getInt("POINTS.POINTS-KOTH")
        val ctmp = config.getInt("POINTS.POINTS-CITADEL")

        val HEADER = LangFile.getStringList("TEAM.POINT-BREAKDOWN.HEADER")
        val FOOTER = LangFile.getStringList("TEAM.POINT-BREAKDOWN.FOOTER")

        val format = LangFile.getString("TEAM.POINT-BREAKDOWN.FORMAT")

        for (line in HEADER) {
            player.sendMessage(translate(line))
        }

        player.sendMessage(
            translate(
                format!!
                    .replace("%points%", (team.kills * kmp).toString())
                    .replace("%multi%", kmp.toString())
                    .replace("%rawPoints%", team.kills.toString())
            )
        )


        player.sendMessage(
            translate(
                format
                    .replace("%points%", "-" + (team.deaths * dmp).toString())
                    .replace("%multi%", dmp.toString())
                    .replace("%rawPoints%", team.deaths.toString())
            )
        )


        player.sendMessage(
            translate(
                format
                    .replace("%points%", (team.kothCaptures * ktmp).toString())
                    .replace("%multi%", ktmp.toString())
                    .replace("%rawPoints%", team.kothCaptures.toString())
            )
        )

        player.sendMessage(
            translate(
                format
                    .replace("%points%", (team.citadelCaptures * ctmp).toString())
                    .replace("%multi%", ctmp.toString())
                    .replace("%rawPoints%", team.citadelCaptures.toString())
            )
        )

        for (line in FOOTER) {
            player.sendMessage(translate(line))
        }

    }

    @Subcommand("top")
    fun top(player: Player) {
        val teams = TeamManager.getTeams().sortedByDescending { it.calculatePoints() }
            .filter { it.leader != null }.take(10)

        var i = 1

        for (line in LangFile.getStringList("TEAM.TEAM-TOP.TEAM_TOP_MSG")) {
            if (line.contains("%team_top%")) {
                for (team in teams) {
                    val tooltip = LangFile.getStringList("TEAM.TEAM-TOP.HOVER-MESSAGE")
                        .map {
                            it.replace("%player%", Bukkit.getOfflinePlayer(team.leader!!.uuid).name!!)
                                .replace("%balance%", NumberFormat.getInstance().format(team.balance))
                                .replace("%kills%", team.kills.toString())
                                .replace("%deaths%", team.deaths.toString())
                                .replace("%koth-captures%", team.kothCaptures.toString())
                        }
                    val format = translate(
                        LangFile.getString("TEAM.TEAM-TOP.TEAM-TOP-FORMAT")!!
                            .replace("%pos%", i.toString())
                            .replace("%team%", if (team.isMember(player.uniqueId)) "&a" else "&c")
                            .replace("%points%", team.calculatePoints().toString())
                    )

                    tooltip.forEach { translate(it) }

                    FancyMessage(format).command("/f who $team")
                        .tooltip(tooltip).send(player)

                    i++
                }
            }

            player.sendMessage(translate(line))
        }
    }

    @Subcommand("rename")
    fun rename(player: Player, @Single @Name("newName") newName: String) {
        val profile = player.getProfile()!!

        if (profile.teamString == null) {
            player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))
            return
        }

        if (!TeamManager.isValidTeamText(newName)) {
            player.sendMessage(translate(LangFile.getString("TEAM.INVALID-NAME")!!))
            return
        }

        if (TeamManager.getTeam(newName) != null) {
            player.sendMessage(translate(LangFile.getString("TEAM.TEAM-ALREADY-EXISTS")!!))
            return
        }

        val team = profile.team!!

        if (!team.isLeader(player.uniqueId)) {
            player.sendMessage(translate(LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!.replace("%role%", "Captain")))
            return
        }

        val copyOfTeam = team.copy(identifier = name.lowercase(), name = name)

        team.members.forEach {
            val memberProfile = HCFPlugin.instance.profileService.getProfile(it.uuid)!!
            memberProfile.teamString = newName
            memberProfile.save()
        }

        TeamManager.delete(team)
        TeamManager.cache[newName.lowercase()] = copyOfTeam
        TeamManager.save(team)

        player.sendMessage(translate(LangFile.getString("TEAM.RENAMED")!!.replace("%name%", newName)))
    }

    @Subcommand("withdraw")
    fun withdraw(player: Player, @Name("amount") amount: Int) {
        val profile = player.getProfile()!!

        // Note for future maybe make configurable
        if (amount < 10) {
            player.sendMessage(translate(LangFile.getString("TEAM.WITHDRAW-ATLEAST")!!))
            return
        }

        if (profile.teamString == null) {
            player.sendMessage(translate(LangFile.getString("TEAM.NOT_IN_TEAM")!!))
            return
        }

        val team = profile.team!!

        if (!team.isAtLeast(player.uniqueId, TeamRole.CAPTAIN)) {
            player.sendMessage(translate(LangFile.getString("TEAM.INSUFFICIENT_ROLE")!!.replace("%role%", "Captain")))
            return
        }

        if (team.balance < amount) {
            player.sendMessage(translate(LangFile.getString("TEAM.INSUFFICIENT_FUNDS")!!))
            return
        }

        team.balance -= amount
        team.save()

        profile.balance = +amount
        profile.save()

        player.sendMessage(
            translate(
                LangFile.getString("TEAM.PLAYER-WITHDRAW")!!.replace("%amount%", amount.toString())
            )
        )
        team.sendTeamMessage(
            translate(
                LangFile.getString("TEAM.TEAM-WITHDRAW")!!.replace("%player%", player.name)
                    .replace("%amount%", amount.toString())
            )
        )
    }

    @Subcommand("list")
    fun list(player: Player, @Optional pageInput: Int?) {
        val page = pageInput ?: 1

        object : BukkitRunnable() {
            override fun run() {
                if (page < 1) {
                    player.sendMessage(translate(LangFile.getString("TEAM.TEAM-LIST.INVALID-PAGE")!!))
                    return
                }

                val teamPlayerCount: HashMap<Team, Int> = hashMapOf()

                for (online in Bukkit.getOnlinePlayers()) {
                    if (!player.canSee(online)) continue

                    val team = online.getProfile()!!.team

                    if (team != null) {
                        if (teamPlayerCount.containsKey(team)) {
                            teamPlayerCount[team] = teamPlayerCount[team]!! + 1
                        } else {
                            teamPlayerCount[team] = 1
                        }
                    }
                }

                val maxPages = teamPlayerCount.size / 10 + 1
                val currentPage = page.coerceAtMost(maxPages)

                val start = (currentPage - 1) * 10
                var index = 0

                for (line in LangFile.getStringList("TEAM.TEAM-LIST.SHOWN_LIST")) {
                    if (index < start) {
                        continue
                    }

                    if (index > start + 10) {
                        break
                    }

                    if (line.contains("%team_list%")) {
                        for (entry in sortByValues(teamPlayerCount)) {
                            val team = entry.key
                            val memberCount = entry.value

                            val tooltip = LangFile.getStringList("TEAM.TEAM-LIST.HOVER-MESSAGE")
                                .map {
                                    it.replace("%dtr%", team.getFormattedDTR()).replace("%hq%", team.getFormattedHQ())
                                }

                            FancyMessage(
                                translate(
                                    LangFile.getString("TEAM.TEAM-LIST.TEAM-LIST-FORMAT")!!
                                        .replace("%team%", team.name)
                                        .replace("%online%", memberCount.toString())
                                        .replace("%max%", team.members.size.toString())
                                        .replace("%pos%", index.toString())
                                )
                            )
                                .tooltip(tooltip.map { translate(it) }).command(
                                    LangFile.getString("TEAM.TEAM-LIST.TEAM-CLICK-COMMAND")!!
                                        .replace("%team%", team.name)
                                )
                                .send(player)

                            index++

                            continue
                        }
                        continue
                    }

                    player.sendMessage(
                        translate(
                            line.replace("%page%", currentPage.toString())
                                .replace("%max-pages%", maxPages.toString())
                        )
                    )
                }
            }
        }.runTaskAsynchronously(HCFPlugin.instance)
    }


    fun sortByValues(map: Map<Team, Int>): LinkedHashMap<Team, Int> {
        val list = LinkedList(map.entries)
        list.sortWith { o1, o2 -> o2.value.compareTo(o1.value) }
        val sortedHashMap = LinkedHashMap<Team, Int>()
        for ((key, value) in list) {
            sortedHashMap[key] = value
        }
        return sortedHashMap
    }
}