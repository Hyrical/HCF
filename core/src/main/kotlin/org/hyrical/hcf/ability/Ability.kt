package org.hyrical.hcf.ability

import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

abstract class Ability(val id: String) : Listener {

    abstract fun getName(): String
    abstract fun getDescription(): String
    abstract fun getItemStack(): ItemStack



}