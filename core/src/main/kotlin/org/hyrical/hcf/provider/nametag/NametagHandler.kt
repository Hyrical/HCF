package org.hyrical.hcf.provider.nametag

import com.lunarclient.bukkitapi.LunarClientAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.config.impl.LunarFile
import org.hyrical.hcf.provider.nametag.packet.NametagPacket
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.plugin.PluginUtils
import org.hyrical.hcf.utils.translate
import org.hyrical.hcf.version.VersionManager
import java.util.UUID
import java.util.concurrent.Executors


class NametagHandler(val adapter: NametagAdapter) {

    val executor = Executors.newSingleThreadExecutor()
    val nametags: HashMap<UUID, Nametag> = hashMapOf()

    fun updateLunarNametags(from: Player, to: Player, update: String){
        if (!PluginUtils.isPlugin("LunarClient-API")) return

        val lines: ArrayList<String> = arrayListOf()

        val team = to.getProfile()!!.team

        if (team != null){
            lines.add(translate(LunarFile.getString("NAMETAGS.NORMAL")!!.replace("%name%", team.getFormattedTeamName(from))
                .replace("%dtr-color%", team.getDTRColor()).replace("%dtr%", team.getDTRFormat().format(team.dtr)
                    .replace("%dtr-symbol%", team.getDTRSymbol()))))
        }

        lines.add(translate(update + to.name))
        LunarClientAPI.getInstance().overrideNametag(to, lines, from)
    }

    fun update() {
        for (player in Bukkit.getOnlinePlayers()){
            for (target in Bukkit.getOnlinePlayers()){
                executor.execute {
                    val update = adapter.getAndUpdate(player, target)
                    updateLunarNametags(player, target, update)
                }
            }
        }
    }

    fun createPacket(player: Player): NametagPacket {
        return NametagVersioning.versionToPacket(VersionManager.getNMSVer()!!).getConstructor(Player::class.java).newInstance(player)
        /*
        val version = "org.hyrical.hcf.provider.nametag.packet.type.NametagPacketV" + VersionManager.getNMSVer()
        return Class.forName(version).getConstructor(Player::class.java)
            .newInstance(player) as NametagPacket

         */
    }

}