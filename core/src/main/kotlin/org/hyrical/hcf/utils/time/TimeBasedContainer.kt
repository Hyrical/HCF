package org.hyrical.hcf.utils.time

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class TimeBasedContainer<E> @JvmOverloads constructor(
    time: Long,
    timeUnit: TimeUnit,
    timeStamps: MutableMap<E, Long> = ConcurrentHashMap(),
) {
    private val expireTime: Long
    private val timeStamps: MutableMap<E, Long>

    init {
        expireTime = timeUnit.toMillis(time)
        check(timeStamps.isEmpty()) { "Time stamps map must be empty" }
        this.timeStamps = timeStamps
    }

    operator fun contains(e: E): Boolean {
        validate()
        return timeStamps.containsKey(e)
    }

    fun add(e: E) {
        validate()
        timeStamps[e] = System.currentTimeMillis()
    }

    fun remove(e: E) {
        validate()
        timeStamps.remove(e)
    }

    fun getExpiryTime(e: E): Long {
        validate()
        return if (timeStamps.containsKey(e)) timeStamps[e]!! + expireTime else -1
    }

    private fun validate() {
        timeStamps.entries.removeIf { (_, value): Map.Entry<E, Long> -> System.currentTimeMillis() > value + expireTime }
    }
}