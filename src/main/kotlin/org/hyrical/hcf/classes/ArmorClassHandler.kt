package org.hyrical.hcf.classes

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.classes.impl.MinerClass
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.utils.translate
import java.util.*


object ArmorClassHandler : Runnable {

    val equippedClasses: MutableMap<UUID, ArmorClass> = mutableMapOf()
    val armorClasses: MutableList<ArmorClass> = mutableListOf()

    init {
        armorClasses.add(MinerClass())

        for (armorClass in armorClasses){
            Bukkit.getPluginManager().registerEvents(armorClass, HCFPlugin.instance)
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(HCFPlugin.instance, this, 2L, 2L)
    }

    override fun run() {
        for (player in Bukkit.getOnlinePlayers()){
            if (equippedClasses.containsKey(player.uniqueId)){
                val armorClass = equippedClasses[player.uniqueId]!!

                if (!armorClass.qualifies(player.inventory)){
                    equippedClasses.remove(player.uniqueId)

                    player.activePotionEffects.clear()
                    armorClass.removeInfiniteEffects(player)

                    player.sendMessage(translate(LangFile.getString("CLASS.DISABLE")!!
                        .replace("%class%", armorClass.name)))

                    return
                }

                armorClass.tick(player)
            } else {
                for (armorClass in armorClasses){
                    if (!armorClass.qualifies(player.inventory)) continue

                    armorClass.apply(player)
                    equippedClasses[player.uniqueId] = armorClass

                    player.sendMessage(translate(LangFile.getString("CLASS.ENABLE")!!
                        .replace("%class%", armorClass.name)))

                    break
                }
            }
        }
    }
    fun hasKitOn(player: Player, armorClass: ArmorClass): Boolean {
        return equippedClasses.containsKey(player.uniqueId) && equippedClasses[player.uniqueId] == armorClass
    }


}