package org.hyrical.hcf.utils.table.expiry

import com.google.common.collect.HashBasedTable

class HashBasedTableExpiryThread<C, R>(private val table: HashBasedTable<C, R, *>, private val expiry: Long) : Thread() {

    val expiryTable: HashBasedTable<C, R, Long> = HashBasedTable.create()

    override fun run() {
        while (true) {
            tick()
            sleep(1000)
        }
    }

    private fun tick() {

    }
}