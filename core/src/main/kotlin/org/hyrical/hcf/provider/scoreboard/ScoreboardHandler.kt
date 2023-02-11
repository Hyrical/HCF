package org.hyrical.hcf.provider.scoreboard

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.provider.scoreboard.adapter.ScoreboardAdapter
import org.hyrical.hcf.provider.scoreboard.adapter.impl.HCFScoreboardAdapter
import org.hyrical.fastboard.FastBoard
import org.hyrical.hcf.provider.scoreboard.listeners.ScoreboardListener
import org.hyrical.hcf.provider.scoreboard.thread.ScoreboardThread
import java.util.UUID

object ScoreboardHandler {

    fun load(){
        ScoreboardThread().start()
        Bukkit.getPluginManager().registerEvents(ScoreboardListener, HCFPlugin.instance)
        HCFPlugin.instance.logger.info("[Scoreboard] Scoreboard loaded succesfully")
    }

    val boards = mutableMapOf<UUID, HCFScoreboard>()
    val adapter: ScoreboardAdapter = HCFScoreboardAdapter()

    fun create(player: Player){
        boards[player.uniqueId] = HCFScoreboard(player, FastBoard(player))
    }

    fun delete(player: Player){
        boards.remove(player.uniqueId)
    }
}