package org.hyrical.hcf.listener

import com.cryptomorin.xseries.XMaterial
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.translate

@org.hyrical.hcf.registry.annotations.Listener
object GeneralListeners : Listener {

    @EventHandler
    fun join(event: PlayerJoinEvent){
        event.joinMessage = ""
        if (!event.player.hasPlayedBefore()){
            val player = event.player

            val bookItem = ItemStack(XMaterial.WRITTEN_BOOK.parseMaterial()!!)
            val bookMeta = bookItem.itemMeta as BookMeta

            bookMeta.title = translate(HCFPlugin.instance.config.getString("JOIN-ITEMS.BOOK.TITLE")!!)
            bookMeta.author = translate(HCFPlugin.instance.config.getString("JOIN-ITEMS.BOOK.AUTHOR")!!)

            for (page in HCFPlugin.instance.config.getStringList("JOIN-ITEMS.BOOK.PAGES")){
                bookMeta.addPage(translate(page))
            }

            bookItem.itemMeta = bookMeta
            player.inventory.addItem(bookItem)

            for (string in HCFPlugin.instance.config.getStringList("JOIN-ITEMS.OTHER-ITEMS")){
                val items = string.split(", ")
                val item = ItemBuilder.of(Material.valueOf(items[0]), items[1].toInt())

                val enchantments = items[2].split(":")

                val enchantment = Enchantment.getByName(enchantments[0])!!
                val enchantmentLevel = enchantments[1].toInt()

                item.enchant(enchantment, enchantmentLevel)
                player.inventory.addItem(item.build())
            }
        }
    }

    @EventHandler
    fun quit(event: PlayerQuitEvent){
        event.quitMessage = ""
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun expGive(event: BlockBreakEvent){
        event.player.giveExp(event.expToDrop)
        event.expToDrop = 0 // automatically gives them the xp if they get any
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun expGive(event: EntityDeathEvent){
        if (event.entity is Player) return

        val killer = event.entity.killer ?: return

        killer.giveExp(event.droppedExp)
        event.droppedExp = 0
    }


}