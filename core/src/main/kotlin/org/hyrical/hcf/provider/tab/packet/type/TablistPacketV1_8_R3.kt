package org.hyrical.hcf.provider.tab.packet.type

import com.google.common.collect.HashBasedTable
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.TabFile
import org.hyrical.hcf.provider.tab.Tab
import org.hyrical.hcf.provider.tab.extra.TabEntry
import org.hyrical.hcf.provider.tab.extra.TabSkin
import org.hyrical.hcf.provider.tab.packet.TabPacket
import org.hyrical.hcf.utils.reflection.ReflectionUtils
import org.hyrical.hcf.version.VersionManager
import java.lang.reflect.Field
import java.util.*


class TablistPacketV1_8_R3(player2: Player) : TabPacket(player2) {
    var LOADED = false
    var footer: String = ""
    val FAKE_PLAYERS: HashBasedTable<Int, Int, EntityPlayer> = HashBasedTable.create()
    var header: String = ""
    val FOOTER_FIELD: Field? = ReflectionUtils.accessField(PacketPlayOutPlayerListHeaderFooter::class.java, "b")
    var maxColumns = if (VersionManager.getProtocolVersion(player) >= 47) 4 else 3

    fun loadFakes() {
        if (!this.LOADED) {
            this.LOADED = true

            val minecraftServer: MinecraftServer = MinecraftServer.getServer()
            val worldServer: WorldServer = minecraftServer.getWorldServer(0)

            //val tab = HCFPlugin.instance.tabHandler.tablists[player.uniqueId]

            for (i in 0..19) {
                for (f in 0..3) {
                    val part = if (f == 0) "LEFT" else if (f == 1) "MIDDLE" else if (f == 2) "RIGHT" else "FAR_RIGHT"
                    val name = this.getName(f, i)
                    val split = name.split(" ")
                    val profile = GameProfile(
                        UUID.randomUUID(),
                        if (name.contains("PLAYER-UUID")) split.subList(2, split.size).joinToString(" ") else name
                    )
                    //if (tab!!.entries[f, i])

                    val player = EntityPlayer(minecraftServer, worldServer, profile, PlayerInteractManager(worldServer))
                    val skin: TabSkin = if (name.contains("PLAYER-UUID")) {
                        Bukkit.broadcastMessage("UUID: " + split[1])
                        HCFPlugin.instance.tabHandler.skins[split[1]]
                    } else {
                        HCFPlugin.instance.tabHandler.skins[name]
                    } ?: HCFPlugin.instance.tabHandler.skins[name]!!
                    profile.properties.put("textures", Property("textures", skin.value, skin.signature))
                    this.FAKE_PLAYERS.put(f, i, player)
                }
            }
        }
    }

    private fun sendPacket(packet: Packet<*>) {
        val playerConnection = (player as CraftPlayer).handle.playerConnection
        playerConnection?.sendPacket(packet)
    }

    fun init(){
        print(maxColumns)
        for (i in 0..19) {
            for (f in 0 until maxColumns) {
                val player: EntityPlayer = this.FAKE_PLAYERS.get(f, i)
                this.sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player))
            }
        }
    }

    init {
        this.loadFakes()
        this.init()
    }

    private fun sendHeaderFooter() {
        /*
        if (maxColumns == 3) {
            return
        }
        //val header: String = java.lang.String.join("\n", HCFPlugin.instance.tabHandler.adapter.getHeader(player).toString())
        //val footer: String = java.lang.String.join("\n", HCFPlugin.instance.tabHandler.adapter.getFooter(player).)
        val header: String = HCFPlugin.instance.tabHandler.adapter.getHeader(player).joinToString { "\n" }
        val footer: String = HCFPlugin.instance.tabHandler.adapter.getFooter(player).joinToString { "\n" }
        if (this.footer == footer && this.header == header) {
            return
        }
        this.header = header
        this.footer = footer
        val packetPlayOutPlayerListHeaderFooter = PacketPlayOutPlayerListHeaderFooter(
            IChatBaseComponent.ChatSerializer.a(
                "{\"text\":\"$header\"}"
            )
        )
        try {
            this.FOOTER_FIELD!!.set(
                packetPlayOutPlayerListHeaderFooter,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"$footer\"}")
            )
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        sendPacket(packetPlayOutPlayerListHeaderFooter)

         */

        val header = HCFPlugin.instance.tabHandler.adapter.getHeader(player)
        val footer = HCFPlugin.instance.tabHandler.adapter.getFooter(player)

        VersionManager.currentVersion.sendHeaderFooter(player, header, footer)
    }


    override fun update() {
        sendHeaderFooter()
        val tablist: Tab = HCFPlugin.instance.tabHandler.adapter.getInfo(player)
        for (i in 0..19) {
            for (f in 0 until maxColumns) {
                val entry: TabEntry = tablist.getEntries(f, i)
                val player: EntityPlayer = this.FAKE_PLAYERS.get(f, i)
                if (player.ping != entry.ping) {
                    player.ping = entry.ping
                    sendPacket(PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, player))
                }
                handleTeams(player.bukkitEntity, entry.text, calcSlot(f, i))
            }
        }
    }

}