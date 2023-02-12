package org.hyrical.hcf.config.impl

import org.hyrical.hcf.config.Configuration

object ReclaimFile : Configuration() {
    override fun getFileName(): String {
        return "reclaims.yml"
    }
}