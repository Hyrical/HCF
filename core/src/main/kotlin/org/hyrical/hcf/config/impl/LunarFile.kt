package org.hyrical.hcf.config.impl

import org.hyrical.hcf.config.Configuration

object LunarFile : Configuration() {
    override fun getFileName(): String {
        return "lunar.yml"
    }


}