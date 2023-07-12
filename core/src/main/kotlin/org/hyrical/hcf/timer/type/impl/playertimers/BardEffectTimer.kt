package org.hyrical.hcf.timer.type.impl.playertimers

import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.timer.type.PlayerTimer

/**
 * Class created on 7/12/2023

 * @author 98ping
 * @project HCF
 * @website https://solo.to/redis
 */
object BardEffectTimer : PlayerTimer(HCFPlugin.instance.config.getInt("TIMERS.BARD-EFFECT"), "BARD-EFFECT")