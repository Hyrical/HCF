package org.hyrical.hcf.provider.nametag.packet.type

import net.minecraft.server.v1_7_R4.Packet
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import org.hyrical.hcf.provider.nametag.extra.NameVisibility
import org.hyrical.hcf.provider.nametag.extra.NametagInfo
import org.hyrical.hcf.provider.nametag.packet.NametagPacket
import org.hyrical.hcf.utils.reflection.ReflectionUtils
import java.lang.reflect.Field


class NametagPacketV1_7_R4(player2: Player) : NametagPacket(player2){
    private val teams: HashMap<String, NametagInfo> = hashMapOf()
    private val teamsByPlayer: HashMap<String, String> = hashMapOf()

    override fun addToTeam(player: Player?, team: String) {
        val name = teamsByPlayer[player!!.name]
        val info: NametagInfo? = teams[team]
        if (name != null && name == team) {
            return
        }
        if (info == null) {
            return
        }
        teamsByPlayer[player.name] = team
        this.sendPacket(ScoreboardPacket(info, 3, listOf(player.name)).toPacket())
    }

    private fun sendPacket(packet: Packet) {
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
        val info: NametagInfo? = teams[name]
        if (info != null) {
            if (info.color != color || info.prefix != prefix || info.suffix != suffix
            ) {
                val infoT = NametagInfo(name, suffix, friendlyInvis, color, visibility, prefix)
                teams[name] = infoT
                sendPacket(ScoreboardPacket(infoT, 2).toPacket())
            }
            return
        }
        val info2 = NametagInfo(name, suffix, friendlyInvis, color, visibility, prefix)
        teams[name] = info2
        sendPacket(ScoreboardPacket(info2, 0).toPacket())

    }

    private class ScoreboardPacket {
        val action: Int
        val players: List<String>
        private val info: NametagInfo

        fun toPacket(): PacketPlayOutScoreboardTeam {
            val team = PacketPlayOutScoreboardTeam()
            a!!.set(team, info.name)
            f!!.set(team, action)
            if (action == 0 || action == 2) {
                b!!.set(team, info.name)
                c!!.set(team, info.prefix + info.color)
                d!!.set(team, info.suffix)
                g!!.set(team, if (info.friendlyInvis) 3 else 0)
            }
            if (action == 3 || action == 4) {
                e!!.set(team, players)
            }
            return team
        }

        constructor(info: NametagInfo, action: Int, players: List<String>) {
            this.info = info
            this.action = action
            this.players = players
        }

        fun getInfo(): NametagInfo {
            return info
        }

        constructor(info: NametagInfo, action: Int) {
            this.info = info
            this.action = action
            players = emptyList()
        }

        companion object {
            private var c: Field? = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "c")
            private var g: Field? = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "g")
            private var a: Field? = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "a")
            private var b: Field? = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "b")
            private var f: Field? = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "f")
            private var d: Field? = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "d")
            private var e: Field? = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "e")

        }
    }
}