package org.hyrical.hcf.config.impl

import org.hyrical.hcf.config.Configuration

object DatabaseFile : Configuration() {
    override fun getFileName(): String {
        return "database.yml"
    }
}