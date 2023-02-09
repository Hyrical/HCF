package org.hyrical.hcf.utils.table.expiry

import org.hyrical.hcf.utils.table.ExpirableHashBasedTable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class ExpiryTask<R, C, V>(private val expirableHashBasedTable: ExpirableHashBasedTable<R, C, V>) : Runnable {

    private var scheduledExecutorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    fun start() {
        scheduledExecutorService.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS)
    }

    override fun run() {
        val currentTime = System.currentTimeMillis()
        for (row in expirableHashBasedTable.expiryTable.rowKeySet()) {
            for (column in expirableHashBasedTable.expiryTable.columnKeySet()) {
                if (expirableHashBasedTable.expiryTable[row, column]!! < currentTime) {
                    expirableHashBasedTable.delegate.remove(row, column)
                    expirableHashBasedTable.expiryTable.remove(row, column)
                }
            }
        }
    }
}
