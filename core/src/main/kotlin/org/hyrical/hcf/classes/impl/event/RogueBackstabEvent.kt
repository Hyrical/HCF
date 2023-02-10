package org.hyrical.hcf.classes.impl.event

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

class RogueBackstabEvent(val backStabbed: Player, val backstabbedBy: Player, val damageCause: DamageCause,
                         val damageAmount: Double
    ) : EntityDamageEvent(backStabbed, damageCause, damageAmount) {


}