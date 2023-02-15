package org.hyrical.hcf.timer

import org.bukkit.Bukkit
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.registry.annotations.Listener
import org.hyrical.hcf.timer.type.impl.playertimers.AppleTimer
import org.hyrical.hcf.timer.type.impl.playertimers.ArcherTag
import org.hyrical.hcf.timer.type.impl.playertimers.CombatTimer
import org.hyrical.hcf.timer.type.impl.playertimers.EnderpearlTimer
import org.hyrical.hcf.utils.reflection.ClassScanner

object TimerHandler {

    fun load(){
        AppleTimer.load()
        ArcherTag.load()
        CombatTimer.load()
        EnderpearlTimer.load()
    }

}