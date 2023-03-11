package org.hyrical.hcf.timer.type.impl.playertimers

import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.timer.type.PlayerTimer

object GlobalAbilityTimer : PlayerTimer(HCFPlugin.instance.config.getInt("TIMERS.GLOBAL-ABILITY"), "GLOBAL-ABILITY")