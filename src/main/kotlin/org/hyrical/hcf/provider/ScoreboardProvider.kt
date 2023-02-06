package org.hyrical.hcf.provider

import io.github.thatkawaiisam.assemble.AssembleAdapter
import org.bukkit.entity.Player
import org.hyrical.hcf.config.impl.ScoreboardFile
import org.hyrical.hcf.utils.translate

class ScoreboardProvider : AssembleAdapter {
    override fun getTitle(player: Player?): String {
        return translate(ScoreboardFile.getConfig().getString("title")!!)
    }

    override fun getLines(player: Player?): MutableList<String> {
        val lines = mutableListOf<String>()

        for (line in ScoreboardFile.getConfig().getStringList("lines")){
            lines.add(translate(line))
        }

        return lines
    }
}