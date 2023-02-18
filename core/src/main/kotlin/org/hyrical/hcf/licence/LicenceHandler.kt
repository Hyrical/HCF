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
            println("I HATE NIGGERS")
            stopAndError()
            return
        }

        val url = URL("https://127.0.0.1:8080/api/licence/$licence")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true
        conn.setRequestProperty("Content-Length", licence.length.toString())

        val outputStream = conn.outputStream
        outputStream.write(licence.toByteArray())
        outputStream.flush()

        /*
        if (conn.responseCode != HttpURLConnection.HTTP_OK) {
            println("EVEN FIGURES")
            stopAndError()
            return
        }

         */

        val response = conn.inputStream.bufferedReader().use { it.readText() }
        if (response != "Success") {
            println("NOPOX IS GAY")
            stopAndError()
            return
        }

        // License verification succeeded
        printSuccess()
    }

    private fun stopAndError() {
        val consoleSender = Bukkit.getConsoleSender()
        consoleSender.sendMessage(translate("&7&m-------------------------------------"))
        consoleSender.sendMessage(translate("&b&lHCF &fmade by &bHyrical&f."))
        consoleSender.sendMessage(translate("&fThe license key in the &bconfig.yml is invalid."))
        consoleSender.sendMessage(translate("&fIf you believe this is an error, contact us via &9Discord&f."))
        consoleSender.sendMessage(translate("&7&m-------------------------------------"))

        Bukkit.getPluginManager().disablePlugin(HCFPlugin.instance)
    }

    private fun printSuccess() {
        val consoleSender = Bukkit.getConsoleSender()
        consoleSender.sendMessage(translate("&7&m-------------------------------------"))
        consoleSender.sendMessage(translate("&b&lHyricalHCF &fhas loaded!"))
        consoleSender.sendMessage(translate("&fCheck your files to &bconfigurate &fthe plugin!"))
        consoleSender.sendMessage(translate("&7&m-------------------------------------"))
    }
}
