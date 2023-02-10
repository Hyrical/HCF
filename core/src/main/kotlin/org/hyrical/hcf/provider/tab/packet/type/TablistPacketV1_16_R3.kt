package org.hyrical.hcf.provider.tab.packet.type

import com.google.common.collect.HashBasedTable
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.server.v1_16_R3.*
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.TabFile
import org.hyrical.hcf.provider.tab.Tab
import org.hyrical.hcf.provider.tab.extra.TabEntry
import org.hyrical.hcf.provider.tab.extra.TabSkin
import org.hyrical.hcf.provider.tab.packet.TabPacket
import org.hyrical.hcf.version.VersionManager
import java.util.*


class TablistPacketV1_16_R3(val player2: Player) : TabPacket(player2) {
    private var LOADED = false
    private var footer: String = ""
    private var header: String = ""
    private val maxColumns = if (VersionManager.getProtocolVersion(player) >= 47) 4 else 3
    private val FAKE_PLAYERS: HashBasedTable<Int, Int, EntityPlayer>? = HashBasedTable.create()

    fun loadFakes() {
        if (!this.LOADED) {
            this.LOADED = true
            val minecraftServer = MinecraftServer.getServer()
            val worldServer: WorldServer = minecraftServer.worlds.iterator().next()
            for (i in 0..19) {
                for (f in 0..3) {
                    val part = if (f == 0) "LEFT" else if (f == 1) "MIDDLE" else if (f == 2) "RIGHT" else "FAR_RIGHT"
                    val line: String = TabFile.getStringList(part)[i].split(";")[0]
                    val profile = GameProfile(UUID.randomUUID(), getName(f, i))
                    val player = EntityPlayer(minecraftServer, worldServer, profile, PlayerInteractManager(worldServer))
                    val skin: TabSkin = HCFPlugin.instance.tabHandler.skins[line]!!
                    profile.properties.put("textures", Property("textures", skin.value, skin.signature))
                    this.FAKE_PLAYERS!!.put(f, i, player)
                }
            }
        }
    }

    private fun sendPacket(packet: Packet<*>) {
        val playerConnection = (player as CraftPlayer).handle.playerConnection
        playerConnection?.sendPacket(packet)
    }

    fun init(){
        for (i in 0..19) {
            for (f in 0..maxColumns) {
                val player: EntityPlayer = this.FAKE_PLAYERS!!.get(f, i)
                sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player))
            }
        }
    }

    init {
        this.loadFakes()
        this.init()
    }

    private fun sendHeaderFooter() {
        val header: String = java.lang.String.join("\n", HCFPlugin.instance.tabHandler.adapter.getHeader(player).toString())
        val footer: String = java.lang.String.join("\n", HCFPlugin.instance.tabHandler.adapter.getFooter(player).toString())
        if (this.footer == header && this.header == header) {
            return
        }
        this.header = header
        this.footer = footer
        val packet = PacketPlayOutPlayerListHeaderFooter()
        packet.header = IChatBaseComponent.ChatSerializer.a("{\"text\":\"$header\"}")
        packet.footer = IChatBaseComponent.ChatSerializer.a("{\"text\":\"$footer\"}")
        sendPacket(packet)
    }


    override fun update() {
        sendHeaderFooter()
        val tablist: Tab = HCFPlugin.instance.tabHandler.adapter.getInfo(player)
        for (i in 0..19) {
            for (f in 0 until maxColumns) {
                val entry: TabEntry = tablist.getEntries(f, i)
                val player: EntityPlayer = this.FAKE_PLAYERS!!.get(f, i)
                if (player.ping != entry.ping) {
                    player.ping = entry.ping
                    sendPacket(
                        PacketPlayOutPlayerInfo(
                            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY,
                            player
                        )
                    )
                }
                handleTeams(player.bukkitEntity, entry.text, calcSlot(f, i))
            }
        }
    }

}