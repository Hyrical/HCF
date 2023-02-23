package org.hyrical.hcf.config.impl

import org.hyrical.hcf.config.Configuration

object StorageFile : Configuration() {
    override fun getFileName(): String {
        return "storage.yml"
    }
}