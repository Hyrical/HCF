package org.hyrical.hcf.classes.impl

import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import org.hyrical.hcf.classes.ArmorClass
import org.hyrical.hcf.classes.ArmorClassHandler
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.utils.translate
import java.util.UUID
import kotlin.math.abs


class RogueClass : ArmorClass("Rogue") {
    private val lastSpeedUsage: HashMap<UUID, Long> = HashMap()
    private val lastJumpUsage: HashMap<UUID, Long> = HashMap()
    private val backstabCooldown: HashMap<UUID, Long> = HashMap()

    override fun apply(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 2))
        player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, Int.MAX_VALUE, 1))
    }

    override fun tick(player: Player) {
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 2))
        }

        if (!player.hasPotionEffect(PotionEffectType.JUMP)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, Int.MAX_VALUE, 1))
        }

        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Int.MAX_VALUE, 0))
        }
    }

    override fun qualifies(armor: PlayerInventory): Boolean {
        return wearingAllArmor(armor) && armor.helmet!!.type == Material.CHAINMAIL_HELMET && armor.chestplate!!.type == Material.CHAINMAIL_CHESTPLATE && armor.leggings!!.type == Material.CHAINMAIL_LEGGINGS && armor.boots!!.type == Material.CHAINMAIL_BOOTS
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onEntityArrowHit(event: EntityDamageByEntityEvent) {
        if (event.isCancelled) {
            return
        }
        if (event.damager is Player && event.entity is Player) {
            val damager = event.damager as Player
            val victim = event.entity as Player
            if (damager.itemInHand.type == Material.GOLDEN_SWORD && ArmorClassHandler.hasKitOn(
                    damager,
                    this
                )
            ) {
                if (backstabCooldown.containsKey(damager.uniqueId) && backstabCooldown[damager.uniqueId]!! > System.currentTimeMillis()) {
                    return
                }
                backstabCooldown[damager.uniqueId] = System.currentTimeMillis() + 1500L
                val playerVector: Vector = damager.location.direction
                val entityVector: Vector = victim.location.direction
                playerVector.setY(0f)
                entityVector.setY(0f)
                val degrees: Float = playerVector.angle(entityVector)
                if (abs(degrees) < 1.4) {
                    damager.setItemInHand(ItemStack(Material.AIR))
                    damager.playSound(damager.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
                    damager.world.playEffect(victim.eyeLocation, Effect.STEP_SOUND, Material.REDSTONE_BLOCK)
                    if (victim.health - 7.0 <= 0) {
                        event.isCancelled = true
                    } else {
                        event.damage = 0.0
                    }

                    victim.health = 0.0.coerceAtLeast(victim.health - 7.0)

                    damager.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 2 * 20, 2))
                } else {
                    damager.sendMessage(translate(LangFile.getString("CLASS.ROGUE.BACKSTAB-FAILED")!!))
                }
            }
        }
    }

}