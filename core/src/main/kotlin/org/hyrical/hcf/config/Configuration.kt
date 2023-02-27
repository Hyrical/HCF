package org.hyrical.hcf.config

import org.bukkit.configuration.file.YamlConfiguration
import org.hyrical.hcf.HCFPlugin
import java.io.File

abstract class Configuration : YamlConfiguration() {
    val file by lazy {  File(HCFPlugin.instance.dataFolder, getFileName()) }

    companion object {
        var config: Configuration? = null
    }

    fun getConfig(): Configuration {
        if (config == null){
            config = this
        }
        return config!!
    }

    fun loadConfig(){
        if (!file.exists()) HCFPlugin.instance.saveResource(file.name, false)

        this.reload()
    }

    abstract fun getFileName(): String

    fun reload() {
        super.load(file)
        super.save(file)
    }

    fun save(){
        super.save(file)
    }

}