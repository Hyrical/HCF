package org.hyrical.hcf.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import org.bukkit.entity.Player
import org.hyrical.hcf.config.impl.ReclaimFile

object ReclaimCommand : BaseCommand() {

    @CommandAlias("reclaim")
    fun reclaim(player: Player){

    }

    fun reclaimPlayer(player: Player){
        val currentList: MutableList<String> = ReclaimFile.getStringList("PLAYERS")

        currentList.add(player.uniqueId.toString())
        ReclaimFile.save()
    }

    fun isReclaimed(player: Player): Boolean {
        val currentList = ReclaimFile.getStringList("PLAYERS")

        return currentList.contains(player.uniqueId.toString())
    }
}
