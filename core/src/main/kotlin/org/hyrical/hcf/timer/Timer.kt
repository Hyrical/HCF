package org.hyrical.hcf.timer

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.config.impl.ScoreboardFile

abstract class Timer : Runnable {

    fun load(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(HCFPlugin.instance, this, 5L, 5L)
    }

    abstract fun getTimerTime(): Long
    abstract fun getConfigPath(): String

    abstract fun applyTimer(player: Player, time: Long)
    abstract fun removeTimer(player: Player)
    abstract fun hasTimer(player: Player): Boolean

    abstract fun getRemainingTime(player: Player): Long

    fun getConfigString(): String {
        return ScoreboardFile.getString(getConfigPath())!!
    }
}