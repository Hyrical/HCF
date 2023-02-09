package org.hyrical.hcf.player

import java.util.UUID

abstract class PlayerHandler {
    open fun getProfile(uuid: UUID): Profile? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getProfile(name: String): Profile? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun all(): List<Profile> {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }
}