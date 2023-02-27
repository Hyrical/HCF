package org.hyrical.hcf.profile.listeners

import com.cryptomorin.xseries.XMaterial
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.profile.ProfileService

@org.hyrical.hcf.registry.annotations.Listener
object OreMinedListener : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!event.block.type.toString().contains("ORE")) return

        val playerProfile = HCFPlugin.instance.profileService.getProfile(event.player.uniqueId) ?: return

        when(event.block.type) {
            XMaterial.COAL_ORE.parseMaterial() -> playerProfile.coalMined++
            XMaterial.IRON_ORE.parseMaterial() -> playerProfile.ironMined++
            XMaterial.GOLD_ORE.parseMaterial() -> playerProfile.goldMined++
            XMaterial.DIAMOND_ORE.parseMaterial() -> playerProfile.diamondMined++
            XMaterial.EMERALD_ORE.parseMaterial() -> playerProfile.emeraldMined++
            XMaterial.LAPIS_ORE.parseMaterial() -> playerProfile.lapisMined++
            XMaterial.REDSTONE_ORE.parseMaterial() -> playerProfile.redstoneMined++
            else -> return
        }

        HCFPlugin.instance.profileService.save(playerProfile)
    }
}