package org.hyrical.hcf.config.impl

import org.hyrical.hcf.config.Configuration

object ScoreboardFile : Configuration() {
    override fun getFileName(): String {
        return "scoreboard.yml"
    }
}