package org.hyrical.hcf.utils.table

import com.google.common.collect.HashBasedTable
import org.hyrical.hcf.utils.table.expiry.ExpiryTask

class ExpirableHashBasedTable<R, C, V>(private val expiry: Long)  {

    val delegate: HashBasedTable<R, C, V> = HashBasedTable.create()
    val expiryTable: HashBasedTable<R, C, Long> = HashBasedTable.create()

    fun clear() = delegate.clear()

    init {
        ExpiryTask(this).start()
    }

    fun columnKeySet(): Set<C> {
        updateExpiryOnAccess()
        return delegate.columnKeySet()
    }

    fun column(columnKey: C): Map<R, V> {
        updateExpiryOnAccess()
        return delegate.column(columnKey)
    }

    fun contains(rowKey: R, columnKey: C): Boolean {
        updateExpiryOnAccess(rowKey, columnKey)
        return delegate.contains(rowKey, columnKey)
    }

    fun containsColumn(columnKey: C): Boolean {
        updateExpiryOnAccess()
        return delegate.containsColumn(columnKey)
    }

    fun containsRow(rowKey: R): Boolean {
        updateExpiryOnAccess()
        return delegate.containsRow(rowKey)
    }

    fun containsValue(value: V): Boolean {
        updateExpiryOnAccess()
        return delegate.containsValue(value)
    }

    fun isEmpty(): Boolean {
        updateExpiryOnAccess()
        return delegate.isEmpty
    }

    fun rowKeySet(): Set<R> {
        updateExpiryOnAccess()
        return delegate.rowKeySet()
    }

    fun row(rowKey: R): Map<C, V> {
        updateExpiryOnAccess()
        return delegate.row(rowKey)
    }

    fun size(): Int {
        updateExpiryOnAccess()
        return delegate.size()
    }

    fun get(rowKey: R, columnKey: C): V? {
        updateExpiryOnAccess(rowKey, columnKey)
        return delegate[rowKey, columnKey]
    }

    fun put(rowKey: R, columnKey: C, value: V): V? {
        expiryTable.put(rowKey, columnKey, System.currentTimeMillis() + expiry)
        return delegate.put(rowKey, columnKey, value)
    }

    private fun updateExpiryOnAccess(rowKey: R? = null, columnKey: C? = null) {
        if (rowKey != null) {
            expiryTable.put(rowKey, columnKey!!, System.currentTimeMillis() + expiry)
        } else {
            for (row in expiryTable.rowKeySet()) {
                for (column in expiryTable.columnKeySet()) {
                    expiryTable.put(row!!, column!!, System.currentTimeMillis() + expiry)
                }
            }
        }
    }
}
