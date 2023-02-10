package org.hyrical.hcf.utils.items

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ItemUtils {

    fun giveItem(player: Player, item: ItemStack){
        if (player.inventory.firstEmpty() == -1) {
            for (content in player.inventory.contents) {
                if (!item.isSimilar(content) || content!!.amount >= content.maxStackSize) continue
                val amount: Int = content.amount + item.getAmount()
                if (amount <= content.maxStackSize) {
                    content.amount = amount
                    return
                }
                content.amount = content.maxStackSize
                item.amount = amount - content.maxStackSize
            }
            player.world.dropItemNaturally(player.location, item)
        } else {
            player.inventory.addItem(item)
            player.updateInventory()
        }
    }

    fun isInventoryFull(player: Player): Boolean {
        val inventory = player.inventory
        for (i in 0 until inventory.size) {
            val item = inventory.getItem(i)
            if (item == null || item.type.isAir) {
                return false
            }
        }
        return true
    }

    fun getItemName(item: ItemStack?): String {
        if (item == null) return "Hand"
        if (item.hasItemMeta() && item.itemMeta!!.hasDisplayName()) return item.itemMeta!!.displayName

        val material = item.type
        val materialName = material.name.toLowerCase().split("_").joinToString(" ") { it.capitalize() }

        return if (material == Material.AIR) "Hand" else materialName
    }
}