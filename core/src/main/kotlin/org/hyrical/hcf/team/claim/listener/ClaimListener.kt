package org.hyrical.hcf.team.claim.listener

import com.cryptomorin.xseries.XMaterial
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.team.claim.LandGrid
import org.hyrical.hcf.team.claim.cuboid.Cuboid
import org.hyrical.hcf.team.claim.logic.ClaimProcessor
import org.hyrical.hcf.utils.translate

object ClaimListener : Listener {


    val claimWand = LandGrid.generateClaimItem()


    @EventHandler
    fun interact(event: PlayerInteractEvent) {
        val player = event.player;

        if (player.itemInHand.isSimilar(claimWand)) {
            val team = TeamManager.search(player.uniqueId) ?: return

            //left click (pos 1)
            if (event.action == Action.LEFT_CLICK_BLOCK) {

                if (event.clickedBlock == null) return

                event.isCancelled = true

                val claimSession: ClaimProcessor =
                    LandGrid.pendingSession.getOrDefault(player.uniqueId, null) ?: return

                claimSession.location1 = event.clickedBlock!!.location
                player.sendMessage(translate("&aUpdated position 1 to &f(" + event.clickedBlock!!.location.blockX + ", " + event.clickedBlock!!.location.blockY + ", " + event.clickedBlock!!.location.blockZ + ")"))
            }


            //right click (pos 2)
            if (event.action == Action.RIGHT_CLICK_BLOCK) {

                if (event.clickedBlock == null) return

                event.isCancelled = true

                val claimSession: ClaimProcessor =
                    LandGrid.pendingSession.getOrDefault(player.uniqueId, null) ?: return

                claimSession.location2 = event.clickedBlock!!.location
                player.sendMessage(translate("&aUpdated position 2 to &f(" + event.clickedBlock!!.location.blockX + ", " + event.clickedBlock!!.location.blockY + ", " + event.clickedBlock!!.location.blockZ + ")"))
            }

            //finalize
            if (event.action == Action.LEFT_CLICK_AIR && player.isSneaking) {
                event.isCancelled = true

                val claimSession: ClaimProcessor =
                    LandGrid.pendingSession.getOrDefault(player.uniqueId, null) ?: return

                if (claimSession.location1 != null && claimSession.location2 != null) {


                    val loc1 = claimSession.location1!!.clone()
                    loc1.y = 256.0

                    val loc2 = claimSession.location2!!.clone()
                    loc2.y = 0.0

                    //opposing world locations
                    if (loc1.world != loc2.world) {
                        player.sendMessage(translate("&cOne of your claim positions are not in the same word as the other!"))
                        return
                    }

                    //land is occupied
                    if (!LandGrid.playerCanClaim(loc1) || !LandGrid.playerCanClaim(loc2)) {
                        player.sendMessage(translate("&cOne or more of the locations in your claim are unable to be claimed!"))
                        return
                    }

                    val claim = Cuboid(loc1, loc2)


                    team.claims.add(claim)
                    team.save()

                    player.setItemInHand(null)
                    player.updateInventory()
                    LandGrid.pendingSession.remove(player.uniqueId)
                    player.sendMessage(translate("&aAdded a team claim for " + team.name))

                } else {
                    player.sendMessage(translate("&cBoth claim positions must be set"))
                }
            }
        }
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        if (!event.itemDrop.itemStack.isSimilar(claimWand)) return

        event.isCancelled = true
    }

}