package org.hyrical.hcf.ability.commands

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.hyrical.hcf.ability.Ability
import org.hyrical.hcf.ability.AbilityService
import org.hyrical.hcf.utils.menus.Button
import org.hyrical.hcf.utils.menus.page.PagedMenu

class AbilitiesMenu : PagedMenu() {
    override fun cancelClicks(): Boolean {
        return true
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val buttons = mutableMapOf<Int, Button>()

        var i = 0

        for (ability in AbilityService.interactAbilities.values) {
            buttons[i] = AbilityButton(ability)
            i++
        }

        for (ability in AbilityService.damageAbilities.values) {
            buttons[i] = AbilityButton(ability)
            i++
        }

        return buttons
    }

    override fun getRawTitle(player: Player): String {
        return "Admin Abilities"
    }

    class AbilityButton(val ability: Ability<out Event>) : Button() {
        override fun getItem(player: Player): ItemStack {
            return ability.getItemStack()
        }

        override fun click(player: Player, slot: Int, clickType: ClickType, hotbarButton: Int) {
            player.inventory.addItem(ability.getItemStack())
        }
    }
}