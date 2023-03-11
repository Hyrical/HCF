package org.hyrical.hcf.team.claim

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.claim.cuboid.Cuboid
import org.hyrical.hcf.team.claim.logic.ClaimProcessor
import org.hyrical.hcf.utils.items.ItemBuilder
import org.hyrical.hcf.utils.translate
import java.util.UUID


object LandGrid {

    val pendingSession = mutableMapOf<UUID, ClaimProcessor>()

    fun findByLocation(location: Location) : Team? {
        return TeamManager.getTeamAtLocation(location)
    }

    fun generateClaimItem() : ItemStack
    {
        val display = HCFPlugin.instance.config.getString("CLAIMS.WAND_DISPLAY")!!
        val lore = HCFPlugin.instance.config.getStringList("CLAIMS.WAND_LORE")
        val item = Material.getMaterial(HCFPlugin.instance.config.getString("CLAIMS.WAND_ITEM")!!.uppercase()) ?: Material.DIAMOND_HOE

        return ItemBuilder.of(item).name(translate(display)).setLore(lore.map { translate(it) }).build()
    }


    fun playerCanClaim(location: Location) : Boolean = findByLocation(location) == null
    fun isOccupied(location: Location) : Boolean = findByLocation(location) != null
}