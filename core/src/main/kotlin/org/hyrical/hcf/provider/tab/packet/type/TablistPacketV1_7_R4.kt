package org.hyrical.hcf.provider.tab.packet.type

import com.google.common.collect.HashBasedTable
import net.minecraft.server.v1_7_R4.*
import net.minecraft.util.com.mojang.authlib.GameProfile
import net.minecraft.util.com.mojang.authlib.properties.Property
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.TabFile
import org.hyrical.hcf.provider.tab.Tab
import org.hyrical.hcf.provider.tab.extra.TabEntry
import org.hyrical.hcf.provider.tab.extra.TabSkin
import org.hyrical.hcf.provider.tab.packet.TabPacket
import org.hyrical.hcf.version.VersionManager
import org.spigotmc.ProtocolInjector.PacketTabHeader
import java.util.*


class TablistPacketV1_7_R4(val player2: Player) : TabPacket(player2) {
    private var LOADED = false
    private var footer: String = ""
    private val FAKE_PLAYERS: HashBasedTable<Int, Int, EntityPlayer> = HashBasedTable.create()
    private var header: String = ""
    private var maxColumns =
        if ((player as CraftPlayer).handle.playerConnection.networkManager.version >= 47) 4 else 3;

    fun loadFakes() {
        if (!this.LOADED) {
            this.LOADED = true

            val minecraftServer: MinecraftServer = MinecraftServer.getServer()
            val worldServer: WorldServer = minecraftServer.getWorldServer(0)
            for (i in 0..19) {
                for (t in 0..3) {
                    val part = if (t == 0) "LEFT" else if (t == 1) "MIDDLE" else if (t == 2) "RIGHT" else "FAR_RIGHT"
                    val line: String = TabFile.getStringList(part)[i].split(";")[0]
                    val profile = GameProfile(UUID.randomUUID(), this.getName(t, i))
                    val entityPlayer =
                        EntityPlayer(minecraftServer, worldServer, profile, PlayerInteractManager(worldServer))
                    val skin: TabSkin = HCFPlugin.instance.tabHandler.skins[line]!!
                    profile.properties.put("textures", Property("textures", skin.value, skin.signature))
                    this.FAKE_PLAYERS.put(t, i, entityPlayer)
                }
            }
        }
    }

    private fun sendPacket(packet: Packet) {
        val playerConnection = (player as CraftPlayer).handle.playerConnection
        playerConnection?.sendPacket(packet)
    }

    fun init(){
        for (i in 0..19) {
            for (f in 0 until maxColumns) {
                val player: EntityPlayer = this.FAKE_PLAYERS.get(f, i)
                this.sendPacket(PacketPlayOutPlayerInfo.addPlayer(player))
            }
        }
    }

    init {
        this.loadFakes()
        this.init()
    }

    private fun sendHeaderFooter() {
        val header = HCFPlugin.instance.tabHandler.adapter.getHeader(player)
        val footer = HCFPlugin.instance.tabHandler.adapter.getFooter(player)

        VersionManager.currentVersion!!.sendHeaderFooter(player, header, footer)
    }


    override fun update() {
        sendHeaderFooter()
        val tablist: Tab = HCFPlugin.instance.tabHandler.adapter.getInfo(player)
        for (i in 0..19) {
            for (f in 0 until maxColumns) {
                val entry: TabEntry = tablist.getEntries(i, f)
                val player: EntityPlayer = this.FAKE_PLAYERS.get(f, i)
                if (player.ping != entry.ping) {
                    player.ping = entry.ping
                    sendPacket(PacketPlayOutPlayerInfo.updatePing(player))
                }
                handleTeams(player.bukkitEntity, entry.text, calcSlot(f, i))
            }
        }
    }

}