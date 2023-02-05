package org.hyrical.hcf.utils.menus.hotbaritem

import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class HotbarItem {
    constructor(player: Player) {
        if (!HOTBAR_ITEMS.containsKey(player.uniqueId)) {
            HOTBAR_ITEMS[player.uniqueId] = ConcurrentHashMap()
        }
        HOTBAR_ITEMS[player.uniqueId]!![this.javaClass.simpleName] = this
    }

    constructor(player: Player, id: String) {
        if (!HOTBAR_ITEMS.containsKey(player.uniqueId)) {
            HOTBAR_ITEMS[player.uniqueId] = ConcurrentHashMap()
        }
        HOTBAR_ITEMS[player.uniqueId]!![id] = this
    }

    abstract val item: ItemStack?
    abstract fun click(action: Action?, block: Block?)
    abstract fun clickEntity(entity: Entity?)
    fun hasCoolDown(): Boolean {
        return true
    }

    companion object {
        val HOTBAR_ITEMS = ConcurrentHashMap<UUID, ConcurrentHashMap<String, HotbarItem>>()
        fun unregisterItem(player: Player, id: String) {
            if (!HOTBAR_ITEMS.containsKey(player.uniqueId)) {
                return
            }
            HOTBAR_ITEMS[player.uniqueId]!!.remove(id)
        }

        fun unregisterItem(player: Player, clazz: Class<out HotbarItem?>) {
            unregisterItem(player, clazz.simpleName)
        }
    }
}