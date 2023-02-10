package org.hyrical.hcf.classes.impl

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.classes.ArmorClass
import org.hyrical.hcf.classes.ArmorClassHandler
import org.hyrical.hcf.classes.impl.bard.BardEffect
import org.hyrical.hcf.config.impl.LangFile
import org.hyrical.hcf.team.Team
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate
import java.util.concurrent.ConcurrentHashMap


class BardClass : ArmorClass("Bard") {
    val BARD_CLICK_EFFECTS: MutableMap<Material, BardEffect> = mutableMapOf()
    val BARD_PASSIVE_EFFECTS: MutableMap<Material, BardEffect> = mutableMapOf()

    private val lastEffectUsage: ConcurrentHashMap<String, Long> = ConcurrentHashMap()
    private val energy: ConcurrentHashMap<String, Float> = ConcurrentHashMap()


    val BARD_RANGE = 20
    val EFFECT_COOLDOWN = 10 * 1000
    val MAX_ENERGY = 100f
    val ENERGY_REGEN_PER_SECOND = 1f

    init {

        // Click buffs


        // Click buffs
        BARD_CLICK_EFFECTS[Material.BLAZE_POWDER] =
            BardEffect.fromPotionAndEnergy(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5, 1), 45)
        BARD_CLICK_EFFECTS[Material.SUGAR] =
            BardEffect.fromPotionAndEnergy(PotionEffect(PotionEffectType.SPEED, 20 * 6, 2), 20)
        BARD_CLICK_EFFECTS[Material.FEATHER] =
            BardEffect.fromPotionAndEnergy(PotionEffect(PotionEffectType.JUMP, 20 * 5, 6), 25)
        BARD_CLICK_EFFECTS[Material.IRON_INGOT] =
            BardEffect.fromPotionAndEnergy(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 2), 40)
        BARD_CLICK_EFFECTS[Material.GHAST_TEAR] =
            BardEffect.fromPotionAndEnergy(PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 2), 40)
        BARD_CLICK_EFFECTS[Material.MAGMA_CREAM] =
            BardEffect.fromPotionAndEnergy(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 45, 0), 40)
        BARD_CLICK_EFFECTS[Material.INK_SAC] =
            BardEffect.fromPotionAndEnergy(PotionEffect(PotionEffectType.INVISIBILITY, 20 * 45, 0), 45)
        //BARD_CLICK_EFFECTS.put(Material.FERMENTED_SPIDER_EYE, BardEffect.fromEnergy(60));
        //BARD_CLICK_EFFECTS.put(Material.FERMENTED_SPIDER_EYE, BardEffect.fromEnergy(60));
        BARD_CLICK_EFFECTS[Material.WHEAT] = BardEffect.fromEnergy(25)

        BARD_CLICK_EFFECTS[Material.SPIDER_EYE] =
            BardEffect.fromPotionAndEnergy(PotionEffect(PotionEffectType.WITHER, 20 * 5, 1), 35)

        // Passive buffs

        // Passive buffs
        BARD_PASSIVE_EFFECTS[Material.BLAZE_POWDER] =
            BardEffect.fromPotion(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6, 0))
        BARD_PASSIVE_EFFECTS[Material.SUGAR] = BardEffect.fromPotion(PotionEffect(PotionEffectType.SPEED, 20 * 6, 1))
        BARD_PASSIVE_EFFECTS[Material.FEATHER] = BardEffect.fromPotion(PotionEffect(PotionEffectType.JUMP, 20 * 6, 1))
        BARD_PASSIVE_EFFECTS[Material.IRON_INGOT] =
            BardEffect.fromPotion(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 6, 0))
        BARD_PASSIVE_EFFECTS[Material.GHAST_TEAR] =
            BardEffect.fromPotion(PotionEffect(PotionEffectType.REGENERATION, 20 * 6, 0))
        BARD_PASSIVE_EFFECTS[Material.MAGMA_CREAM] =
            BardEffect.fromPotion(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 6, 0))
        BARD_PASSIVE_EFFECTS[Material.INK_SAC] =
            BardEffect.fromPotion(PotionEffect(PotionEffectType.INVISIBILITY, 20 * 5, 0))
        //BARD_PASSIVE_EFFECTS.put(Material.FERMENTED_SPIDER_EYE, BardEffect.fromPotion(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 6, 0)));

        object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (!ArmorClassHandler.hasKitOn(player, this@BardClass)) continue

                    if (energy.containsKey(player.name)) {
                        if (energy[player.name] == MAX_ENERGY) {
                            continue
                        }
                        energy[player.name] = MAX_ENERGY.coerceAtMost(energy[player.name]!! + ENERGY_REGEN_PER_SECOND)
                    } else {
                        energy[player.name] = 0f
                    }
                    val manaInt: Int = energy[player.name]!!.toInt()
                    if (manaInt % 10 == 0) {
                        player.sendMessage(translate(LangFile.getString("CLASS.BARD.ENERGY")!!))
                    }
                }
            }
        }.runTaskTimer(HCFPlugin.instance, 15L, 20L)
    }

    override fun apply(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Int.MAX_VALUE, 1), true)
        player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, Int.MAX_VALUE, 0), true)
    }

    override fun tick(player: Player) {
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1))
        }

        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Int.MAX_VALUE, 1))
        }

        if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, Int.MAX_VALUE, 0))
        }

        if (BARD_PASSIVE_EFFECTS.containsKey(player.itemInHand.type)) {
            if (player.itemInHand.type === Material.INK_SAC && player.itemInHand.durability != 0.toShort()) {
                return
            }

            // CUSTOM
            if (player.itemInHand.type === Material.FERMENTED_SPIDER_EYE && lastEffectUsage.containsKey(player.name) && lastEffectUsage.get(
                    player.name
                )!! > System.currentTimeMillis()
            ) {
                return
            }

            giveBardEffect(player, BARD_PASSIVE_EFFECTS[player.itemInHand.type]!!,
                friendly = true,
                persistOldValues = false
            )
        }
    }

    fun giveBardEffect(source: Player, bardEffect: BardEffect, friendly: Boolean, persistOldValues: Boolean) {
        for (player in getNearbyPlayers(source, friendly)) {

            if (ArmorClassHandler.hasKitOn(
                    player,
                    this
                ) && bardEffect.potionEffect != null && bardEffect.potionEffect.type
                    .equals(PotionEffectType.INCREASE_DAMAGE)
            ) {
                continue
            }
            if (bardEffect.potionEffect != null) {
                smartAddPotion(player, bardEffect.potionEffect, persistOldValues, this)
            } else {
                val material = source.itemInHand.type
                giveCustomBardEffect(player, material)
            }
        }
    }

    fun giveCustomBardEffect(player: Player, material: Material) {
        when (material) {
            Material.WHEAT -> for (nearbyPlayer in getNearbyPlayers(player, true)) {
                nearbyPlayer.foodLevel = 20
                nearbyPlayer.saturation = 10f
            }

            Material.FERMENTED_SPIDER_EYE -> {}
            else -> HCFPlugin.instance.logger.warning("No custom Bard effect defined for $material.")
        }
    }

    fun getNearbyPlayers(player: Player, friendly: Boolean): List<Player> {
        val valid: MutableList<Player> = ArrayList()
        val sourceTeam: Team? = player.getProfile()!!.team

        // We divide by 2 so that the range isn't as much on the Y level (and can't be abused by standing on top of / under events)
        for (entity in player.getNearbyEntities(
            BARD_RANGE.toDouble(),
            (BARD_RANGE / 2).toDouble(),
            BARD_RANGE.toDouble()
        )) {
            if (entity is Player) {
                val nearbyPlayer = entity

                if (sourceTeam == null) {
                    if (!friendly) {
                        valid.add(nearbyPlayer)
                    }
                    continue
                }

                val isFriendly: Boolean = sourceTeam.isMember(nearbyPlayer.uniqueId)

                if (friendly && isFriendly) {
                    valid.add(nearbyPlayer)
                } else if (!friendly && !isFriendly) { // the isAlly is here so you can't give your allies negative effects, but so you also can't give them positive effects.
                    valid.add(nearbyPlayer)
                }
            }
        }
        valid.add(player)
        return valid
    }

    override fun qualifies(armor: PlayerInventory): Boolean {
        return wearingAllArmor(armor) &&
                armor.helmet!!.type == Material.GOLDEN_HELMET &&
                armor.chestplate!!.type == Material.GOLDEN_CHESTPLATE &&
                armor.leggings!!.type == Material.GOLDEN_LEGGINGS &&
                armor.boots!!.type == Material.GOLDEN_BOOTS
    }
}