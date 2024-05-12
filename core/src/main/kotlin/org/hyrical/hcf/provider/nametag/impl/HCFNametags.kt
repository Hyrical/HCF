package org.hyrical.hcf.provider.nametag.impl

import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.provider.nametag.Nametag
import org.hyrical.hcf.provider.nametag.NametagAdapter
import org.hyrical.hcf.provider.nametag.extra.NameVisibility
import org.hyrical.hcf.team.TeamManager
import org.hyrical.hcf.timer.type.impl.playertimers.ArcherTag
import org.hyrical.hcf.utils.getProfile
import org.hyrical.hcf.utils.translate


class HCFNametags : NametagAdapter {
    private fun createTeam(
        from: Player,
        to: Player,
        id: String,
        prefix: String,
        suffix: String,
        visibility: NameVisibility
    ): String {
        val nametag: Nametag? = HCFPlugin.instance.nametagHandler.nametags[from.uniqueId]
        val displayName = if (prefix.isEmpty()) "" else "$prefix "
        if (nametag != null) {
            nametag.nametagPacket.create(id, suffix, translate(displayName), "", true, visibility)
            nametag.nametagPacket.addToTeam(to, id)
        }
        return displayName + suffix
    }

    override fun getAndUpdate(from: Player, to: Player): String {
        val profile = to.getProfile()!!
        val team = from.getProfile()!!.team

        if (team != null && team.isMember(to.uniqueId) || from == to){
            return this.createTeam(from, to, "team", HCFPlugin.instance.config.getString("RELATION-COLOR.TEAMMATE")!!, "", NameVisibility.ALWAYS)
        }

        if (profile.pvpTimer > System.currentTimeMillis()){
            return this.createTeam(from, to, "pvp-timer", HCFPlugin.instance.config.getString("RELATION-COLOR.PVP-TIMER")!!, "", NameVisibility.ALWAYS)
        }

        if (to.hasPotionEffect(PotionEffectType.INVISIBILITY) && from != to){
            return this.createTeam(from, to, "invis", "", "", NameVisibility.NEVER)
        }

        if (ArcherTag.hasTimer(to)){
            return this.createTeam(from, to, "archer-tag", HCFPlugin.instance.config.getString("RELATION-COLOR.ARCHER-TAG")!!, "", NameVisibility.ALWAYS)
        }

        return this.createTeam(from, to, "enemy", HCFPlugin.instance.config.getString("RELATION-COLOR.ENEMY")!!, "", NameVisibility.ALWAYS)
    }
}