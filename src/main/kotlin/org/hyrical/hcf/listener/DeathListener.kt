package org.hyrical.hcf.listener

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.PlayerDeathEvent
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.utils.getLang
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.items.ItemUtils
import org.hyrical.hcf.utils.translate
import java.text.NumberFormat
import kotlin.math.max
import kotlin.math.min


object DeathListener : Listener {

    private val path = "DEATH.MESSAGES"

    @org.hyrical.hcf.registry.annotations.Listener
    fun death(event: PlayerDeathEvent){
        val victim = event.entity
        val killer = event.entity.killer

        val cause = if (victim.lastDamageCause == null) DamageCause.SUICIDE else victim.lastDamageCause!!.cause

        val world = victim.world

        world.strikeLightningEffect(victim.location)

        val team = victim.getProfile()!!.team

        if (team != null){
            for (s in LangFile.getStringList("TEAM.MEMBER-LISTENER.MEMBER-DEATH")){
                team.sendTeamMessage(translate(s))
            }
        }

        event.deathMessage = handleDeath(victim, killer, event, cause)
    }

    fun handleDeath(victim: Player, killer: Player?, event: PlayerDeathEvent, cause: DamageCause): String {
        val victimProfile = victim.getProfile()!!

        if (killer != null){
            val killerProfile = killer.getProfile()!!

            killerProfile.kills++
            killerProfile.killstreak++
            killerProfile.highestKillstreak = victimProfile.killstreak
            killerProfile.balance += victimProfile.balance
            killerProfile.save()

            if (victimProfile.balance > 100){
                killer.sendMessage(translate(LangFile.getString("DEATH.BALANCE-RECEIVED")!!.replace("%player%", victim.displayName).replace("%money%", NumberFormat.getInstance().format(victimProfile.balance))))
            }
        }

        victimProfile.deaths++
        victimProfile.highestKillstreak = victimProfile.killstreak
        victimProfile.killstreak = 0
        victimProfile.balance = 0
        victimProfile.save()

        val victimTeam = victimProfile.team

        if (victimTeam != null){
            victimTeam.deaths++

            victimTeam.save()
        }

        if (victim.lastDamageCause is EntityDamageByEntityEvent){
            when (cause){
                DamageCause.FALLING_BLOCK -> return LangFile.getString("$path.SUFFOCATION")!!.replace("%player%", formatName(victim))
                DamageCause.BLOCK_EXPLOSION -> return LangFile.getString("$path.EXPLOSION")!!.replace("%player%", formatName(victim))
                DamageCause.LIGHTNING -> return LangFile.getString("$path.LIGHTNING")!!.replace("%player%", formatName(victim))
                DamageCause.ENTITY_ATTACK -> {
                    if (killer is Player){
                        return LangFile.getString("$path.KILLER")!!.replace("%player%", formatName(victim))
                            .replace("%killer%", formatName(killer)).replace("%item%", ItemUtils.getItemName(killer.itemInHand))
                    }
                    return LangFile.getString("$path.ENTITY")!!.replace("%player%", formatName(victim))
                        .replace("%entity%", event.entity.type.name.lowercase().capitalize())
                }
                DamageCause.FALL -> {
                    if (killer != null && killer != victim){
                        val distance = killer.location.distance(victim.location).toInt()

                        return LangFile.getString("$path.FALL_KILLER")!!.replace("%player%", formatName(victim))
                            .replace("%killer%", formatName(killer)).replace("%blocks%", distance.toString())
                    }
                    return LangFile.getString("$path.FALL")!!.replace("%player%", formatName(victim))
                }
                DamageCause.PROJECTILE -> {
                    if (killer != null && killer != victim){
                        val distance = killer.location.distance(victim.location).toInt()
                        return LangFile.getString("$path.PROJECTILE_KILLER")!!.replace("%player%", formatName(victim))
                            .replace("%killer%", formatName(killer)).replace("%blocks%", distance.toString())
                    }
                    return LangFile.getString("$path.PROJECTILE")!!.replace("%player%", formatName(victim))
                }

                else -> return LangFile.getString("$path.DEFAULT")!!.replace("%player%", formatName(victim))
            }
        } else {
            when (cause){
                DamageCause.BLOCK_EXPLOSION -> return LangFile.getString("$path.EXPLOSION")!!.replace("%player%", formatName(victim))
                DamageCause.WITHER -> return LangFile.getString("$path.WITHER")!!.replace("%player%", formatName(victim))
                DamageCause.STARVATION -> return LangFile.getString("$path.STARVATION")!!.replace("%player%", formatName(victim))
                DamageCause.DROWNING -> return LangFile.getString("$path.DROWNING")!!.replace("%player%", formatName(victim))
                DamageCause.SUFFOCATION -> return LangFile.getString("$path.SUFFOCATION")!!.replace("%player%", formatName(victim))
                DamageCause.POISON -> return LangFile.getString("$path.POISON")!!.replace("%player%", formatName(victim))
                DamageCause.LAVA -> return LangFile.getString("$path.LAVA")!!.replace("%player%", formatName(victim))
                DamageCause.FIRE, DamageCause.FIRE_TICK -> return LangFile.getString("$path.FIRE")!!.replace("%player%", formatName(victim))
                DamageCause.CONTACT, DamageCause.THORNS -> return LangFile.getString("$path.CONTACT")!!.replace("%player%", formatName(victim))
                DamageCause.FALL -> {
                    if (killer != null && killer != victim){
                        val distance = killer.location.distance(victim.location).toInt()

                        return LangFile.getString("$path.FALL_KILLER")!!.replace("%player%", formatName(victim))
                            .replace("%killer%", formatName(killer)).replace("%blocks%", distance.toString())
                    }
                    return LangFile.getString("$path.FALL")!!.replace("%player%", formatName(victim))
                }
                DamageCause.VOID -> {
                    if (killer != null && killer != victim){
                        return LangFile.getString("$path.VOID_KILLER")!!.replace("%player%", formatName(victim)).replace("%killer%", formatName(killer))
                    }
                    return LangFile.getString("$path.VOID")!!.replace("%player%", formatName(victim))
                }

                else -> return LangFile.getString("$path.DEFAULT")!!.replace("%player%", formatName(victim))
            }
        }
    }

    fun formatName(player: Player): String {
        val profile = player.getProfile()!!

        return LangFile.getString("TEAM.MEMBER-LISTENER.DEATH-NAME-FORMAT")!!.replace("%player%",
            player.name).replace("%kills%", profile.kills.toString())
    }
}