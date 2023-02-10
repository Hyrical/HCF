package org.hyrical.hcf.provider.nametag.packet.type

import net.minecraft.server.v1_8_R3.Packet
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.hyrical.hcf.provider.nametag.extra.NameVisibility
import org.hyrical.hcf.provider.nametag.extra.NametagInfo
import org.hyrical.hcf.provider.nametag.packet.NametagPacket
import org.hyrical.hcf.utils.reflection.ReflectionUtils
import java.lang.reflect.Field


class NametagPacketV1_8_R3(player2: Player) : NametagPacket(player2) {
    private val teamsByPlayer: HashMap<String, String> = hashMapOf()
    private val teams: HashMap<String, NametagInfo> = hashMapOf()

    override fun addToTeam(player: Player?, team: String) {
        val playerTeam: String? = this.teamsByPlayer[player!!.name]
        val info: NametagInfo? = this.teams[team]
        if (playerTeam != null && playerTeam == team) {
            return
        }
        if (info == null) {
            return
        }
        this.teamsByPlayer[player.name] = team
        this.sendPacket(ScoreboardPacket(info, 3, listOf(player.name)).toPacket())
    }

    private fun sendPacket(packet: Packet<*>) {
        (player as CraftPlayer).handle.playerConnection?.sendPacket(packet)
    }

    override fun create(
        name: String,
        color: String,
        prefix: String,
        suffix: String,
        friendlyInvis: Boolean,
        visibility: NameVisibility
    ) {
        val info1: NametagInfo? = teams[name]
        if (info1 != null) {
            if (info1.color != color || info1.prefix != prefix || info1.suffix != suffix
            ) {
                val info2 = NametagInfo(name, suffix, friendlyInvis, color, visibility, prefix)
                teams[name] = info2
                sendPacket(ScoreboardPacket(info2, 2).toPacket())
            }
            return
        }
        val info3 = NametagInfo(name, suffix, friendlyInvis, color, visibility, prefix)
        teams[name] = info3
        sendPacket(ScoreboardPacket(info3, 0).toPacket())
    }


    private class ScoreboardPacket {
        private val info: NametagInfo
        private val action: Int
        private val players: List<String>

        constructor(info: NametagInfo, action: Int) {
            this.info = info
            this.action = action
            players = emptyList()
        }

        constructor(info: NametagInfo, action: Int, players: List<String>) {
            this.info = info
            this.action = action
            this.players = players
        }

        fun toPacket(): PacketPlayOutScoreboardTeam {
            val scoreboardTeam = PacketPlayOutScoreboardTeam()
            a!!.set(scoreboardTeam, info.name)
            h!!.set(scoreboardTeam, action)
            if (action == 0 || action == 2) {
                b!!.set(scoreboardTeam, info.name)
                c!!.set(scoreboardTeam,
                    if (info.prefix.isEmpty() && info.color
                            .isEmpty()
                    ) "" else info.prefix + info.color
                )
                d!!.set(scoreboardTeam, info.suffix)
                i!!.set(scoreboardTeam, if (info.friendlyInvis) 3 else 0)
                e!!.set(scoreboardTeam, info.visibility.nameDisplay)
            }
            if (action == 3 || action == 4) {
                g!!.set(scoreboardTeam, players)
            }
            return scoreboardTeam
        }

        companion object {
            private var b: Field? = null
            private var c: Field? = null
            private var g: Field? = null
            private var d: Field? = null
            private var i: Field? = null
            private var e: Field? = null
            private var a: Field? = null
            private var h: Field? = null

            init {
                a = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "a")
                b = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "b")
                c = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "c")
                d = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "d")
                i = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "i")
                h = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "h")
                g = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "g")
                e = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "e")
            }
        }
    }
}