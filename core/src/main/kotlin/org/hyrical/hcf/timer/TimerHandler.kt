package org.hyrical.hcf.timer

import org.bukkit.Bukkit
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.registry.annotations.Listener
import org.hyrical.hcf.timer.type.impl.playertimers.*
import org.hyrical.hcf.utils.reflection.ClassScanner

object TimerHandler {

    fun load(){
        AppleTimer.load()
        ArcherTag.load()
        CombatTimer.load()
        EnderpearlTimer.load()
        LogoutTimer.load()

        HCFPlugin.instance.logger.info("[Timer] Timers loaded succesfully")

    }

}