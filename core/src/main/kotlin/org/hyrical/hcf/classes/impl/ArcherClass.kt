package org.hyrical.hcf.classes.impl

import com.cryptomorin.xseries.XMaterial
import com.cryptomorin.xseries.XPotion
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.potion.PotionEffect
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.classes.ArmorClass
import org.hyrical.hcf.config.impl.ClassFile
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.provider.nametag.NametagHandler
import org.hyrical.hcf.timer.type.impl.playertimers.ArcherTag
import org.hyrical.hcf.utils.translate
import java.util.*
import kotlin.math.max

class ArcherClass : ArmorClass("Archer", arrayListOf(
    XMaterial.LEATHER_HELMET.parseMaterial()!!,
    XMaterial.LEATHER_CHESTPLATE.parseMaterial()!!,
    XMaterial.LEATHER_LEGGINGS.parseMaterial()!!,
    XMaterial.LEATHER_BOOTS.parseMaterial()!!,
)) {

    val arrowForce: MutableMap<UUID, Float> = mutableMapOf()

    override fun tick(player: Player) {}

    override fun apply(player: Player) {
        player.addPotionEffect(PotionEffect(XPotion.SPEED.potionEffectType!!, Int.MAX_VALUE, 2))
        player.addPotionEffect(PotionEffect(XPotion.DAMAGE_RESISTANCE.potionEffectType!!, Int.MAX_VALUE, 0))
    }

    @EventHandler
    fun hitArrow(event: EntityDamageByEntityEvent){
        if (event.damager !is Arrow) return
        if (event.entity !is Player) return

        tag(event)
    }

    @EventHandler
    fun shoot(event: EntityShootBowEvent){
        if (event.entity !is Player) return
        if (event.projectile !is Arrow) return

        arrowForce[event.projectile.uniqueId] = event.force
    }

    fun tag(event: EntityDamageByEntityEvent){ // probably want to make a function for this just in case we want to use it for some kind of ability of some sort.
        val arrow = event.damager as Arrow

        val damager = getDamager(event.damager) ?: return
        val victim = event.entity as Player

        if (!arrowForce.containsKey(arrow.uniqueId)) return

        var tagDamage = if (ArcherTag.hasTimer(victim))
            ClassFile.getDouble("CLASSES.ARCHER.ARCHER-TAGGED-DAMAGE") else ClassFile.getDouble("CLASSES.ARCHER.ARCHER-TAG-DAMAGE")

        val force = arrowForce.remove(arrow.uniqueId)!!
        val dist = victim.location.distance(victim.location)

        if (force <= 0.5){
            tagDamage = HCFPlugin.instance.config.getDouble("CLASSES.ARCHER.ARCHER-HALF-FORCE-TAG")
        }

        val formattedDamage = tagDamage / 2.0

        victim.health = max(damager.health - tagDamage, 0.0)

        event.damage = 0.0

        if (victim.isDead){
            victim.lastDamageCause = EntityDamageByEntityEvent(damager, victim, EntityDamageEvent.DamageCause.PROJECTILE, tagDamage)
            event.isCancelled = true
        }

        if (force >= 0.5f){
            damager.sendMessage(translate(
                LangFile.getString("CLASSES.ARCHER.MARKED")!!
                    .replace("%distance%", dist.toString()).replace("%seconds%", (ArcherTag.time / 1000L).toString())
                    .replace("%damage%", formattedDamage.toString())
            ))

            if (!ArcherTag.hasTimer(victim)){
                victim.sendMessage(translate(
                    LangFile.getString("CLASSES.ARCHER.MARKED-PLAYER")!!
                        .replace("%seconds%", (ArcherTag.time / 1000L).toString())
                ))
            }

            ArcherTag.applyTimer(victim, true)
        } else {
            damager.sendMessage(translate(
                LangFile.getString("CLASSES.ARCHER.NOT-DRAWN-BACK")!!
                    .replace("%distance%", dist.toString())
                    .replace("%damage%", formattedDamage.toString())
            ))
        }
    }

    fun getDamager(entity: Entity): Player? {
        if (entity is Player) {
            return entity
        }
        if (entity is Projectile) {
            val projectile: Projectile = entity
            if (projectile.shooter is Player) {
                return projectile.shooter as Player
            }
        }
        return null
    }


}