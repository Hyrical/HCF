package org.hyrical.hcf.config.impl

import org.hyrical.hcf.config.Configuration

object TabFile : Configuration() {
    override fun getFileName(): String {
        return "tab.yml"
    }
}