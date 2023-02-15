package org.hyrical.webapi.type

import org.hyrical.store.Storable

data class Licence(
    override val identifier: String
) : Storable