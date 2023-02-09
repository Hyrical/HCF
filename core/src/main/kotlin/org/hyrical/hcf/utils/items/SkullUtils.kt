package org.hyrical.hcf.utils.items

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.hyrical.hcf.utils.translate
import java.lang.reflect.Field
import java.util.*

object SkullUtils {
    fun applyCustomHead(
        skull: ItemStack,
        value: String,
        signature: String,
        tempName: String,
        lore: MutableList<String>,
        displayname: String
    ): ItemStack? {

        val gameProfile = GameProfile(UUID.randomUUID(), tempName)
        gameProfile.properties.put("textures", Property("textures", value, signature))

        val skullMeta = skull.itemMeta as SkullMeta
        skullMeta.owner = tempName
        skullMeta.lore = lore.map { translate(it) }

        skullMeta.setDisplayName(translate(displayname))

        try {
            val profileField: Field = skullMeta.javaClass.getDeclaredField("profile")
            profileField.isAccessible = true
            profileField.set(skullMeta, gameProfile)
        } catch (exception: NoSuchFieldException) {
            return null
        } catch (exception: IllegalArgumentException) {
            return null
        } catch (exception: IllegalAccessException) {
            return null
        }

        skull.itemMeta = skullMeta

        return skull
    }

}