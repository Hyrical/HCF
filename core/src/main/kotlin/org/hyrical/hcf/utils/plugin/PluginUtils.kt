package org.hyrical.hcf.utils.plugin

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.lang.reflect.Method

object PluginUtils {

    fun getPlugin(name: String): Plugin? {
        return Bukkit.getPluginManager().getPlugin(name)
    }

    fun isPlugin(name: String): Boolean {
        return getPlugin(name) != null
    }

    fun getOnlinePlayers(): List<Player> {
        val server = Bukkit.getServer()
        val method: Method = server.javaClass.getMethod("getOnlinePlayers")
        val result = method.invoke(server)

        if (result is Array<*>) {
            return result.asList() as List<Player>
        } else if (result is Collection<*>) {
            return result as List<Player>
        }

        return emptyList()
    }

    fun getPlayerHealth(player: Player): Double {
        val method = player.javaClass.getMethod("getHealth")
        val result = method.invoke(player)

        if (result is Int) {
            return result.toDouble()
        } else {
            return result as Double
        }
    }

    fun setPlayerHealth(player: Player, health: Double) {
        val method: Method = player.javaClass.getMethod("setHealth", Double::class.java)
        method.invoke(player, health)
    }
}