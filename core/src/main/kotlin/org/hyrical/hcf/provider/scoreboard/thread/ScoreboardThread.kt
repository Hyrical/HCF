package org.hyrical.hcf.provider.scoreboard.thread

import org.hyrical.hcf.provider.scoreboard.ScoreboardHandler

class ScoreboardThread : Thread() {

    override fun run() {
        while (true) {
            try {
                sleep(200)
                tick()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun tick() {
        for (entry in ScoreboardHandler.boards) {
            val board = entry.value

            board.updateBoard()
        }
    }
}