package org.hyrical.hcf.server

import org.hyrical.hcf.HCFPlugin

object ServerHandler {

    private val config = HCFPlugin.instance.config

    var isHCF: Boolean = config.getBoolean("SERVER-MODES.HCF")
    var isUHCF: Boolean = config.getBoolean("SERVER-MODES.UHCF")
    var isSoup: Boolean = config.getBoolean("SERVER-MODES.SOUP")
    var isKitMap: Boolean = config.getBoolean("SERVER-MODES.KITMAP")

    var preEotw: Boolean = false
    var eotw: Boolean = false
}