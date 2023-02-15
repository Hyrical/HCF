package org.hyrical.hcf.provider.nametag.packet.type

import net.minecraft.server.v1_16_R3.EnumChatFormat
import net.minecraft.server.v1_16_R3.Packet
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage
import org.bukkit.entity.Player
import org.hyrical.hcf.provider.nametag.extra.NameVisibility
import org.hyrical.hcf.provider.nametag.extra.NametagInfo
import org.hyrical.hcf.provider.nametag.packet.NametagPacket
import org.hyrical.hcf.utils.reflection.ReflectionUtils
import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashMap


class NametagPacketV1_16_R3(player2: Player) : NametagPacket(player2) {
    private val teamsByPlayer: HashMap<String, String> = hashMapOf()
    private val teams: HashMap<String, NametagInfo> = hashMapOf()

    override fun addToTeam(player: Player?, team: String) {
        val playerTeam = teamsByPlayer[player!!.name]
        val info: NametagInfo? = teams[team]
        if (playerTeam != null && playerTeam == team) {
            return
        }
        if (info == null) {
            return
        }
        teamsByPlayer[player.name] = team
        sendPacket(ScoreboardPacket(info, 3, listOf(player.name)).toPacket())
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

    private fun sendPacket(packet: Packet<*>) {
        (player as CraftPlayer).handle.playerConnection?.sendPacket(packet)
    }

    private class ScoreboardPacket {
        private val action: Int
        private val players: List<String>
        private val info: NametagInfo

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
            val team = PacketPlayOutScoreboardTeam()
            a!!.set(team, info.name)
            i!!.set(team, action)
            if (action == 0 || action == 2) {
                b!!.set(team, CraftChatMessage.fromStringOrNull(info.name))
                c!!.set(team, CraftChatMessage.fromStringOrNull(info.prefix))
                d!!.set(team, CraftChatMessage.fromStringOrNull(info.suffix))
                g!!.set(team, getFormat(info.color))
                j!!.set(team, if (info.friendlyInvis) 3 else 0)
                e!!.set(team, info.visibility.nameDisplay)
            }
            if (action == 3 || action == 4) {
                h!!.set(team, players)
            }
            return team
        }

        fun getFormat(lllllllllllllllllIIIllIlIlIlIllI: String): EnumChatFormat {
            val lllllllllllllllllIIIllIlIlIlIlIl: EnumChatFormat? = formats!![lllllllllllllllllIIIllIlIlIlIllI]
            return if (lllllllllllllllllIIIllIlIlIlIlIl == null) EnumChatFormat.RESET else lllllllllllllllllIIIllIlIlIlIlIl
        }

        companion object {
            private var e: Field? = null
            private var i: Field? = null
            private var formats: Map<String, EnumChatFormat>? = null
            private var h: Field? = null
            private var g: Field? = null
            private var d: Field? = null
            private var j: Field? = null
            private var a: Field? = null
            private var b: Field? = null
            private var c: Field? = null

            init {
                a = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "a")
                b = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "b")
                c = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "c")
                d = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "d")
                e = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "e")
                g = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "g")
                h = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "h")
                i = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "i")
                j = ReflectionUtils.accessField(PacketPlayOutScoreboardTeam::class.java, "j")
                formats = Arrays.stream(EnumChatFormat.values())
                    .collect(Collectors.toMap(EnumChatFormat::toString) {
                            lllllllllllllllllIIIllIlIlIIlIlI -> lllllllllllllllllIIIllIlIlIIlIlI })
            }
        }
    }
}