package org.hyrical.hcf.player

import java.util.UUID

abstract class PlayerHandler {
    open fun getProfile(uuid: UUID): HCFProfile? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getProfile(name: String): HCFProfile? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun all(): List<HCFProfile> {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }
}