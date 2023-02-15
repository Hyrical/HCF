package org.hyrical.hcf.utils.time

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.roundToInt


object TimeUtils {
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm")

    /**
     * Delegate to TimeUtils#formatIntoMMSS for backwards compat
     */
    fun formatIntoHHMMSS(secs: Int): String {
        return formatIntoMMSS(secs)
    }

    /**
     * Formats the time into a format of HH:MM:SS. Example: 3600 (1 hour) displays as '01:00:00'
     *
     * @param secs The input time, in seconds.
     * @return The HH:MM:SS formatted time.
     */
    fun formatIntoMMSS(secs: Int): String {
        // Calculate the seconds to display:
        var secs = secs
        val seconds = secs % 60
        secs -= seconds

        // Calculate the minutes:
        var minutesCount = (secs / 60).toLong()
        val minutes = minutesCount % 60
        minutesCount -= minutes
        val hours = minutesCount / 60
        return (if (hours > 0) (if (hours < 10) "0" else "") + hours + ":" else "") + (if (minutes < 10) "0" else "") + minutes + ":" + (if (seconds < 10) "0" else "") + seconds
    }

    fun formatFancy(value: Long): String {
        return if (value >= 60) {
            formatIntoMMSS(value.toInt())
        } else {
            ((10.0 * value).roundToInt() / 10.0).toString() + "s"
        }
    }

    /**
     * Formats time into a detailed format. Example: 600 seconds (10 minutes) displays as '10 minutes'
     *
     * @param secs The input time, in seconds.
     * @return The formatted time.
     */
    fun formatIntoDetailedString(secs: Int): String {
        if (secs == 0) {
            return "0 seconds"
        }
        val remainder = secs % 86400
        val days = secs / 86400
        val hours = remainder / 3600
        val minutes = remainder / 60 - hours * 60
        val seconds = remainder % 3600 - minutes * 60
        val fDays = if (days > 0) " " + days + " day" + (if (days > 1) "s" else "") else ""
        val fHours = if (hours > 0) " " + hours + " hour" + (if (hours > 1) "s" else "") else ""
        val fMinutes = if (minutes > 0) " " + minutes + " minute" + (if (minutes > 1) "s" else "") else ""
        val fSeconds = if (seconds > 0) " " + seconds + " second" + (if (seconds > 1) "s" else "") else ""
        return (fDays + fHours + fMinutes + fSeconds).trim { it <= ' ' }
    }

    /**
     * Formats time into a format of MM/dd/yyyy HH:mm.
     *
     * @param date The Date instance to format.
     * @return The formatted time.
     */
    fun formatIntoCalendarString(date: Date?): String {
        return dateFormat.format(date)
    }

    /**
     * Parses a string, such as '1h4m25s' into a number of seconds.
     *
     * @param time The string to attempt to parse.
     * @return The number of seconds 'in' the given string.
     */
    fun parseTime(time: String): Int {
        if (time == "0" || time == "") {
            return 0
        }
        val lifeMatch = arrayOf("w", "d", "h", "m", "s")
        val lifeInterval = intArrayOf(604800, 86400, 3600, 60, 1)
        var seconds = 0
        for (i in lifeMatch.indices) {
            val matcher: Matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(time)
            while (matcher.find()) {
                seconds += matcher.group(1).toInt() * lifeInterval[i]
            }
        }
        return seconds
    }

    /**
     * Gets the seconds between date A and date B. This will never return a negative number.
     *
     * @param a Date A
     * @param b Date B
     * @return The number of seconds between date A and date B.
     */
    fun getSecondsBetween(a: Date, b: Date): Int {
        return abs((a.time - b.time) as Int / 1000)
    }
}