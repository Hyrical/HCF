package org.hyrical.hcf.team.commands.menu

import com.cryptomorin.xseries.XMaterial
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.system.Flag
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.menus.Button
import org.hyrical.hcf.utils.menus.page.PagedMenu
import org.hyrical.hcf.utils.translate

/**
 * Class created on 7/11/2023

 * @author 98ping
 * @project HCF
 * @website https://solo.to/redis
 */
class SystemTeamMenu : PagedMenu() {
    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        var button = 0
        val buttons = mutableMapOf<Int, Button>()

        for (team in TeamManager.getTeams().filter { it.leader == null }) {
            buttons[button++] = SystemTeamButton(team)
        }

        return buttons
    }

    override fun getRawTitle(player: Player): String {
        return translate("&7[Editor] &eSystem Teams")
    }

    class SystemTeamButton(val team: Team) : Button() {
        override fun getItem(player: Player): ItemStack {
            return ItemBuilder.of(getWoolColor(getColor()).parseMaterial()!!)
                .name(translate(getColor() + team.name))
                .data(getWoolColor(getColor()).data.toShort())
                .setLore(
                    getDesc()
                )
                .build()
        }

        fun getDesc(): MutableList<String> {
            val desc = mutableListOf<String>()
            desc.add(" ")
            desc.add(translate("&eName: &f" + getColor() + team.name))
            desc.add(translate("&eFlags: &c" + team.flags.size))
            desc.add(translate("&eFlag List"))
            for (flag in team.flags) {
                desc.add(translate("&7- " + Flag.getColor(flag) + flag.displayName))
            }
            desc.add(" ")
            return desc
        }


        fun getColor(): String {
            if (team.flags.isEmpty()) return "&f"
            return Flag.getColor(team.flags.first())
        }

        fun getWoolColor(color: String): XMaterial {
            if (color.contains("&1")) return XMaterial.BLUE_WOOL
            if (color.contains("&2")) return XMaterial.GREEN_WOOL
            if (color.contains("&3")) return XMaterial.CYAN_WOOL
            if (color.contains("&4")) return XMaterial.RED_WOOL
            if (color.contains("&5")) return XMaterial.PURPLE_WOOL
            if (color.contains("&6")) return XMaterial.ORANGE_WOOL
            if (color.contains("&7")) return XMaterial.LIGHT_GRAY_WOOL
            if (color.contains("&8")) return XMaterial.GRAY_WOOL
            if (color.contains("&9")) return XMaterial.BLUE_WOOL
            if (color.contains("&a")) return XMaterial.LIME_WOOL
            if (color.contains("&b")) return XMaterial.LIGHT_BLUE_WOOL
            if (color.contains("&c")) return XMaterial.RED_WOOL
            if (color.contains("&d")) return XMaterial.PINK_WOOL
            return if (color.contains("&e")) XMaterial.YELLOW_WOOL else XMaterial.WHITE_WOOL
        }

    }
}