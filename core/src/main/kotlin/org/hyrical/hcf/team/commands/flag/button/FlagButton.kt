package org.hyrical.hcf.team.commands.flag.button

import com.cryptomorin.xseries.XMaterial
import ltd.matrixstudios.alchemist.util.menu.Button
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.system.Flag
import org.hyrical.hcf.utils.translate


class FlagButton(val team: Team, val flag: Flag, val has: Boolean) : Button() {
    override fun getData(player: Player): Short {
        return 0
    }

    override fun getDescription(player: Player): MutableList<String> {
        val lore: MutableList<String> = mutableListOf()

        lore.add("")
        lore.add("&fClick here to " + (if (has) "&cremove" else "&aadd") +
                "&fthe ${Flag.getColor(flag) + flag.displayName} &fflag.")
        lore.add("")

        return lore
    }

    override fun getDisplayName(player: Player): String? {
        return translate(Flag.getColor(flag) + flag.displayName)
    }

    override fun getMaterial(player: Player): Material {
        return if (has) XMaterial.GREEN_CONCRETE.parseMaterial()!! else
            XMaterial.RED_CONCRETE.parseMaterial()!!
    }

    override fun onClick(player: Player, slot: Int, type: ClickType) {
        if (!has){
            team.flags.add(flag)
            team.save()
            player.sendMessage(translate("&aYou have added this flag to the team."))
        } else {
            team.flags.remove(flag)
            team.save()
            player.sendMessage(translate("&cYou have removed this flag from the team."))
        }
    }
}