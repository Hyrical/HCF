package org.hyrical.hcf.utils.menus.page

import org.bukkit.entity.Player
import org.hyrical.hcf.utils.menus.Button
import org.hyrical.hcf.utils.menus.Menu
import kotlin.math.ceil

abstract class PagedMenu : Menu() {

    var page = 1
    abstract fun getAllPagesButtons(player: Player): Map<Int, Button>
    abstract fun getRawTitle(player: Player): String
    fun getPages(player: Player): Int {
        val buttonAmount = getAllPagesButtons(player).size
        return if (buttonAmount == 0) {
            1
        } else ceil(buttonAmount / getMaxItemsPerPage(player).toDouble()).toInt()
    }

    fun modPage(player: Player, mod: Int) {
        page += mod
        getButtons(player).clear()
        openMenu(player)
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val minIndex = ((page - 1).toDouble() * getMaxItemsPerPage(player)).toInt()
        val maxIndex = (page.toDouble() * getMaxItemsPerPage(player)).toInt()
        val buttons: HashMap<Int, Button> = HashMap<Int, Button>()
        buttons[0] = PageButton(-1, this)
        buttons[8] = PageButton(1, this)
        for (entry in getAllPagesButtons(player).entries) {
            var ind = entry.key
            if (ind in minIndex until maxIndex) {
                ind -= (getMaxItemsPerPage(player).toDouble() * (page - 1)).toInt() - 9
                buttons[ind] = entry.value
            }
        }
        val global: Map<Int, Button>? = getGlobalButtons(player)
        if (global != null) {
            for ((key, value) in global) {
                buttons[key] = value
            }
        }
        return buttons
    }

    open fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    open fun getGlobalButtons(player: Player): Map<Int, Button>? {
        return null
    }

    override fun getTitle(player: Player): String {
        return "(" + page + "/" + getPages(player) + ") " + getRawTitle(player)
    }
}