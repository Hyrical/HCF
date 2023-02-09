package org.hyrical.hcf.utils.menus.listener

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.hyrical.hcf.utils.menus.Button
import org.hyrical.hcf.utils.menus.Menu
import org.hyrical.hcf.utils.menus.hotbaritem.HotbarItem
import org.hyrical.hcf.utils.menus.page.PagedMenu
import org.hyrical.hcf.utils.time.TimeBasedContainer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


object MenuListener : Listener {

    private val clickCooldown: TimeBasedContainer<UUID> = TimeBasedContainer(500, TimeUnit.MILLISECONDS)
    private val entityCooldown: TimeBasedContainer<UUID> = TimeBasedContainer(500, TimeUnit.MILLISECONDS)

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryClick(event: InventoryClickEvent) {
        val player: Player = event.whoClicked as Player
        val menu: Menu = Menu.openedMenus[player] ?: return
        if (event.clickedInventory == null) {
            return
        }
        if (event.clickedInventory !== player.openInventory.topInventory) {
            if (menu.cancelLowerClicks()) event.isCancelled = true
            return
        }
        if (menu.cancelClicks()) {
            event.isCancelled = true
        }
        val slot: Int = event.slot
        val buttons: Map<Int, Button> = menu.getButtons(player)
        if (buttons.containsKey(slot)) {
            val button: Button = buttons[slot]!!
            button.click(player, slot, event.click, event.hotbarButton)
            event.isCancelled = button.isCancelClick
            val sound: Button.ButtonClickSound? = button.getClickSound(player)
            if (sound != null) {
                player.playSound(player.location, sound.sound, sound.volume, sound.pitch)
            }
            if (menu.isClickUpdate) {
                menu.updateInventory(player, menu is PagedMenu)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player: Player = event.player as Player
        val menu: Menu = Menu.openedMenus[player] ?: return
        menu.cancelIncomingUpdates = true
        menu.onClose(player, event.inventory)
        if (menu.updateRunnable != null) {
            menu.updateRunnable!!.cancel()
            menu.updateRunnable = null
        }
        Menu.openedMenus.remove(player)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player: Player = event.player
        val menu: Menu = Menu.openedMenus[player] ?: return
        menu.onClose(player, event.player.openInventory.topInventory)
        if (menu.updateRunnable != null) {
            menu.updateRunnable!!.cancel()
            menu.updateRunnable = null
        }
        Menu.openedMenus.remove(player)
        HotbarItem.HOTBAR_ITEMS.remove(player.uniqueId)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player: Player = event.player
        val items: Map<String, HotbarItem> = HotbarItem.HOTBAR_ITEMS.getOrDefault(
            player.uniqueId,
            ConcurrentHashMap()
        )
        if (event.item == null || event.item!!.type == Material.AIR) {
            return
        }
        if (event.action == Action.PHYSICAL) {
            return
        }
        for (item in items.values) {
            if (item.item?.isSimilar(event.item) == true) {
                event.isCancelled = true
                if (clickCooldown.contains(player.uniqueId)) continue
                item.click(event.action, event.clickedBlock)
                if (item.hasCoolDown()) clickCooldown.add(player.uniqueId)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val player: Player = event.player
        val items: Map<String, HotbarItem> = HotbarItem.HOTBAR_ITEMS.getOrDefault(
            player.uniqueId,
            ConcurrentHashMap()
        )
        if (event.player.itemInHand.type == Material.AIR) {
            return
        }
        for (item in items.values) {
            if (item.item?.isSimilar(player.itemInHand) == true) {
                event.isCancelled = true
                if (entityCooldown.contains(player.uniqueId)) continue
                item.clickEntity(event.rightClicked)
                if (item.hasCoolDown()) entityCooldown.add(player.uniqueId)
            }
        }
    }
}