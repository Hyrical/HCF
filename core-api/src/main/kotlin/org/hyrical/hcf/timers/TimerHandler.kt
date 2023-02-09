package org.hyrical.hcf.timers

abstract class TimerHandler {
    open fun all(): List<HCFTimer> {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }

    open fun getTimerByName(name: String): HCFTimer? {
        throw UnsupportedOperationException("Hyrical HCF not found!")
    }
}