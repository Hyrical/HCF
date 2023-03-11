package org.hyrical.hcf.team.claim

import com.cryptomorin.xseries.XMaterial
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

<<<<<<< Updated upstream:core/src/main/kotlin/org/hyrical/hcf/team/claim/LandGrid.kt
    fun findByLocation(location: Location) : Team? {
        return TeamManager.getTeamAtLocation(location)
    }

=======
>>>>>>> Stashed changes:core/src/main/kotlin/org/hyrical/hcf/team/claim/LandBoard.kt
    fun generateClaimItem() : ItemStack
    {
        val display = HCFPlugin.instance.config.getString("CLAIMS.WAND_DISPLAY")!!
        val lore = HCFPlugin.instance.config.getStringList("CLAIMS.WAND_LORE")
        val item = XMaterial.valueOf(HCFPlugin.instance.config.getString("CLAIMS.WAND_ITEM")!!.uppercase()).parseMaterial() ?: XMaterial.DIAMOND_HOE.parseMaterial()!!

        return ItemBuilder.of(item).name(translate(display)).setLore(lore.map { translate(it) }).build()
    }
<<<<<<< Updated upstream:core/src/main/kotlin/org/hyrical/hcf/team/claim/LandGrid.kt


    fun playerCanClaim(location: Location) : Boolean = findByLocation(location) == null
    fun isOccupied(location: Location) : Boolean = findByLocation(location) != null
=======
>>>>>>> Stashed changes:core/src/main/kotlin/org/hyrical/hcf/team/claim/LandBoard.kt
}