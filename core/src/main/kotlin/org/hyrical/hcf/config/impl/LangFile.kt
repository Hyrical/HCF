package org.hyrical.hcf.config.impl

import org.hyrical.hcf.config.Configuration

object LangFile : Configuration() {
    override fun getFileName(): String {
        return "lang.yml"
    }
}