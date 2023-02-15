package org.hyrical.hcf.licence

import org.bukkit.Bukkit
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.utils.translate
import java.net.HttpURLConnection
import java.net.URL

object LicenceHandler {
    fun verify() {
        val licence = HCFPlugin.instance.config.getString("LICENSE-KEY")

        if (licence == null) {
            stopAndError()
            return
        }

        val url = URL("http://127.0.0.1:8080/api/licence/$licence")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true
        conn.setRequestProperty("Content-Length", licence.length.toString())

        val outputStream = conn.outputStream
        outputStream.write(licence.toByteArray())
        outputStream.flush()

        if (conn.responseCode != HttpURLConnection.HTTP_OK) {
            stopAndError()
            return
        }

        val response = conn.inputStream.bufferedReader().use { it.readText() }
        if (response != "Success") {
            stopAndError()
            return
        }

        // License verification succeeded
        printSuccess()
    }

    private fun stopAndError() {
        val consoleSender = Bukkit.getConsoleSender()
        consoleSender.sendMessage(translate("&7&m-------------------------------------"))
        consoleSender.sendMessage(translate("&4&lHCF &7- &4Invalid license key!"))
        consoleSender.sendMessage(translate("&4&lHCF &7- &4If you believe this is an error, please contact a &d&lDEVELOPER&4."))
        consoleSender.sendMessage(translate("&7&m-------------------------------------"))

        Bukkit.shutdown()
    }

    private fun printSuccess() {
        val consoleSender = Bukkit.getConsoleSender()
        consoleSender.sendMessage(translate("&7&m-------------------------------------"))
        consoleSender.sendMessage(translate("&a&lHCF &7- &aValid license key!"))
        consoleSender.sendMessage(translate("&7&m-------------------------------------"))
    }
}
