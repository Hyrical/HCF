package org.hyrical.hcf.config.impl

import org.hyrical.hcf.config.Configuration

object ClassFile : Configuration() {
    override fun getFileName(): String {
        return "classes.yml"
    }
}