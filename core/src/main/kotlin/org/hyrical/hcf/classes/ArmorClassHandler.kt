package org.hyrical.hcf.classes

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.classes.impl.ArcherClass
import org.hyrical.hcf.classes.impl.MinerClass
import org.hyrical.hcf.classes.impl.RogueClass
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.utils.translate
import java.util.*


object ArmorClassHandler : Runnable {

    val equippedClasses: MutableMap<UUID, ArmorClass> = mutableMapOf()
    val armorClasses: MutableList<ArmorClass> = mutableListOf()

    private val config = HCFPlugin.instance.config

    fun load(){
        /*
        if (config.getBoolean("CLASSES.MINER")){
            armorClasses.add(MinerClass())
        }

        if (config.getBoolean("CLASSES.ROGUE")){
            armorClasses.add(RogueClass())
        }

        if (config.getBoolean("CLASSES.BARD")){
            armorClasses.add(BardClass())
        }

         */

        armorClasses.add(ArcherClass())
        armorClasses.add(MinerClass())
        armorClasses.add(RogueClass())

        for (armorClass in armorClasses){
            Bukkit.getPluginManager().registerEvents(armorClass, HCFPlugin.instance)
        }

        Bukkit.getScheduler().runTaskTimer(HCFPlugin.instance, this, 5L, 5L)
    }

    override fun run() {
        for (player in Bukkit.getOnlinePlayers()){
            if (equippedClasses.containsKey(player.uniqueId)){
                val armorClass = equippedClasses[player.uniqueId]!!

                if (!armorClass.isWearing(player.inventory)){
                    equippedClasses.remove(player.uniqueId)

                    player.activePotionEffects.clear()
                    armorClass.removeEffects(player)

                    player.sendMessage(translate(LangFile.getString("CLASS.UNEQUIP")!!
                        .replace("%class%", armorClass.name)))

                    return
                }

                armorClass.tick(player)
            } else {
                for (armorClass in armorClasses){
                    if (!armorClass.isWearing(player.inventory)) continue

                    armorClass.apply(player)
                    equippedClasses[player.uniqueId] = armorClass

                    player.sendMessage(translate(LangFile.getString("CLASS.EQUIP")!!
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