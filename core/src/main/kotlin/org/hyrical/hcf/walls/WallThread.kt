package org.hyrical.hcf.walls

class WallThread : Thread() {

    override fun run() {
        while (true) {
            try {
                sleep(250)
                tick()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun tick() {
        //
    }
}