package org.hyrical.hcf.utils.menus

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.hyrical.axios.Axios
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.menus.Button.Companion.createPlaceholder
import org.hyrical.hcf.utils.menus.fill.FillTemplate
import org.hyrical.hcf.utils.menus.fill.IMenuFiller
import org.hyrical.hcf.utils.menus.page.PagedMenu
import kotlin.math.ceil

abstract class Menu(val testSize: Int = -1) {
    private var buttons: MutableMap<Int, Button> = HashMap()
    private var inventory: Inventory? = null

    var cancelIncomingUpdates = false

    var updateRunnable: BukkitTask? = null
    abstract fun getTitle(player: Player): String
    abstract fun getButtons(player: Player): MutableMap<Int, Button>

    fun calculateSize(buttons: MutableMap<Int, Button>): Int {
        if (testSize != -1) {
            return testSize
        }

        var highest = 0
        for (buttonValue in buttons.keys) {
            if (buttonValue > highest) {
                highest = buttonValue
            }
        }
        return (ceil((highest + 1) / 9.0) * 9.0).toInt()
    }

    fun openMenu(player: Player) {
        buttons = getButtons(player)
        val size = if (size == -1) calculateSize(buttons) else size
        var update = false
        var title = getTitle(player)
        if (title.length > 32) {
            title = title.substring(0, 32)
        }
        var inventory = Bukkit.createInventory(player, size, title)
        val previousMenu = openedMenus[player]

        if (player.openInventory.topInventory != null) {
            if (previousMenu != null) {
                previousMenu.cancelIncomingUpdates = true
                if (previousMenu.updateRunnable != null) previousMenu.updateRunnable!!.cancel()
            }

            val previousSize = player.openInventory.topInventory.size
            val previousTitle = player.openInventory.topInventory.title
            if (previousSize == size && previousTitle.equals(title, ignoreCase = true)) {
                inventory = player.openInventory.topInventory
                update = true
            }
        }
        if (menuFiller != null) menuFiller!!.fill(this, player, buttons, size)
        for ((key, value) in buttons) inventory.setItem(key, value.getItem(player))
        for (i in inventory.contents.indices) {
            if (buttons[i] == null && inventory.getItem(i) != null && inventory.getItem(i)!!.type != Material.AIR) {
                inventory.setItem(i, ItemBuilder(Material.AIR).build())
            }
        }
        this.inventory = inventory
        if (update) {
            player.updateInventory()
        } else {
            player.openInventory(this.inventory!!)
        }
        startUpdateTask(player, this is PagedMenu)
        onOpen(player)
        openedMenus[player] = this
        cancelIncomingUpdates = false

        //AxiosSounds.send(player, AxiosSounds.MENU_OPEN)
    }

    open fun onOpen(player: Player) {}
    open fun onClose(player: Player, inventory: Inventory) {}
    var isAutoUpdate: Boolean = true
    var isClickUpdate: Boolean = true
    var size: Int = -1
    val fillTemplate: FillTemplate?
        get() = null
    val menuFiller: IMenuFiller?
        get() = fillTemplate?.menuFiller

    fun getPlaceholderItem(player: Player): ItemStack {
        return createPlaceholder().getItem(player)
    }

    open fun cancelLowerClicks(): Boolean {
        return true
    }

    open fun cancelClicks(): Boolean {
        return true
    }

    /*
    private fun updateInventory(player: Player, title: String, items: List<net.minecraft.world.item.ItemStack>) {
        val ep = (player as CraftPlayer).handle

        val itemsPacket = PacketPlayOutWindowItems(ep.activeContainer.windowId, items)
        ep.playerConnection.sendPacket(itemsPacket)
        ep.updateInventory(ep.activeContainer)
    }
     */

    fun startUpdateTask(player: Player, pagedMenu: Boolean) {
        if (!isAutoUpdate) {
            return
        }
        if (updateRunnable != null) {
            return
        }
        updateRunnable = object : BukkitRunnable() {
            override fun run() {
                if (player == null || !player.isOnline) {
                    cancel()
                    return
                }
                updateInventory(player, pagedMenu)
            }
        }.runTaskTimerAsynchronously(Axios.instance, 20L, 20L)
    }

    fun updateInventory(player: Player, pagedMenu: Boolean) {
        if (cancelIncomingUpdates) {
            return
        }
        buttons = getButtons(player)
        val size = if (size == -1) calculateSize(buttons) else size
        if (menuFiller != null) menuFiller!!.fill(this, player, buttons, size)
        for ((key, value) in buttons) inventory!!.setItem(key, value.getItem(player))
        for (i in inventory!!.contents.indices) {
            if (buttons[i] == null && inventory!!.getItem(i) != null && inventory!!.getItem(i)!!.type != Material.AIR) {
                inventory!!.setItem(i, ItemBuilder(Material.AIR).build())
            }
        }
        //player.openInventory.topInventory.setContents(inventory!!.contents.filterNotNull().toTypedArray())
    }

    fun getSlot(row: Int, slot: Int): Int {
        return 9 * row + slot
    }

    companion object {
        val openedMenus: MutableMap<Player, Menu> = HashMap()
    }
}