package org.hyrical.hcf.classes.impl

import com.cryptomorin.xseries.XBiome
import com.cryptomorin.xseries.XMaterial
import com.cryptomorin.xseries.XPotion
import com.cryptomorin.xseries.XSound
import com.cryptomorin.xseries.particles.XParticle
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.hyrical.hcf.classes.ArmorClass
import org.hyrical.hcf.classes.ArmorClassHandler
import org.hyrical.hcf.classes.event.RogueBackstabEvent
import org.hyrical.hcf.config.impl.ClassFile
import org.hyrical.hcf.utils.plugin.PluginUtils
import org.hyrical.hcf.utils.time.TimeUtils
import org.hyrical.hcf.utils.translate
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class RogueClass : ArmorClass("Rogue", arrayListOf(
    XMaterial.CHAINMAIL_HELMET.parseMaterial()!!,
    XMaterial.CHAINMAIL_CHESTPLATE.parseMaterial()!!,
    XMaterial.CHAINMAIL_LEGGINGS.parseMaterial()!!,
    XMaterial.CHAINMAIL_BOOTS.parseMaterial()!!
)) {

    val cooldown: MutableMap<UUID, Long> = mutableMapOf()

    override fun tick(player: Player) {}

    override fun apply(player: Player) {
        player.addPotionEffect(PotionEffect(XPotion.SPEED.potionEffectType!!, Int.MAX_VALUE, 2))
        player.addPotionEffect(PotionEffect(XPotion.JUMP.potionEffectType!!, Int.MAX_VALUE, 1))
        player.addPotionEffect(PotionEffect(XPotion.DAMAGE_RESISTANCE.potionEffectType!!, Int.MAX_VALUE, 0))
    }

    @EventHandler
    fun entityDamage(event: EntityDamageByEntityEvent){
        if (event.damager !is Player || event.entity !is Player) return

        val damager = event.damager as Player
        val victim = event.entity as Player

        if (damager.itemInHand.type == XMaterial.GOLDEN_SWORD.parseMaterial() && ArmorClassHandler.hasKitOn(damager, this)){
            if (cooldown.containsKey(damager.uniqueId) && cooldown[damager.uniqueId]!! > System.currentTimeMillis()) return damager.sendMessage(translate(ClassFile.getString("COOLDOWN-MSG")!!.replace("%seconds%", TimeUtils.formatFancy(
                    cooldown[damager.uniqueId]!! - System.currentTimeMillis()
            ))))

            val damageVector = damager.location.direction
            val victimVector = victim.location.direction

            damageVector.setY(0)
            victimVector.setY(0)

            val angle = damageVector.angle(victimVector)

            if (abs(angle) < 1.4){
                cooldown[damager.uniqueId] = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(
                    ClassFile.getInt("ROGUE.BACKSTAB-COOLDOWN").toLong())

                damager.setItemInHand(ItemStack(Material.AIR))

                damager.playSound(damager.location, XSound.ENTITY_ITEM_BREAK.parseSound()!!, 1f, 1f)
                damager.world.playEffect(victim.eyeLocation, Effect.STEP_SOUND, XMaterial.REDSTONE_BLOCK)

                victim.playSound(damager.location, XSound.ENTITY_ITEM_BREAK.parseSound()!!, 1f, 1f)

                if (PluginUtils.getPlayerHealth(victim) - 7.0 <= 0){
                    event.isCancelled = true
                } else {
                    event.setDamage(0.0)
                }

                victim.lastDamageCause = RogueBackstabEvent(victim, damager, EntityDamageEvent.DamageCause.CUSTOM, 7.0)

                PluginUtils.setPlayerHealth(victim, (PluginUtils.getPlayerHealth(victim) - 7.0).coerceAtLeast(0.0))

                event.setDamage(0.0)

                if (victim.isDead){
                    Bukkit.getPluginManager().callEvent(RogueBackstabEvent(victim, damager, EntityDamageEvent.DamageCause.CUSTOM, 7.0))
                }

                damager.addPotionEffect(PotionEffect(XPotion.SLOW.potionEffectType!!, 2 * 20, 1))
            } else {
                damager.sendMessage(translate(ClassFile.getString("ROGUE.BACKSTAB-FAILED")!!))
            }
        }
    }
}