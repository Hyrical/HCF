package org.hyrical.hcf.team.commands.flag

import ltd.matrixstudios.alchemist.util.menu.Button
import ltd.matrixstudios.alchemist.util.menu.pagination.PaginatedMenu
import org.bukkit.entity.Player
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.commands.flag.button.FlagButton
import org.hyrical.hcf.team.system.Flag

class FlagMenu(player2: Player, val team: Team) : PaginatedMenu(9 * 3, player2) {
    override fun getPagesButtons(player: Player): MutableMap<Int, Button> {
        val buttons: MutableMap<Int, Button> = mutableMapOf()
        var index = 0

        Flag.values().forEach {
            val hasFlag = team.flags.contains(it)
            buttons[index++] = FlagButton(team, it, hasFlag)
        }

        return buttons
    }

    override fun getTitle(player: Player): String {
        return "Flags"
    }
}