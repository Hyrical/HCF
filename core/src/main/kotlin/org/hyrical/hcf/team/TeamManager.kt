package org.hyrical.hcf.team

import org.bukkit.Location
import org.hyrical.hcf.HCFPlugin
import org.hyrical.hcf.server.ServerHandler
import org.hyrical.hcf.storage.StorageService
import org.hyrical.hcf.team.dtr.DTRHandler
import org.hyrical.store.DataStoreController
import org.hyrical.store.type.StorageType


object TeamManager {

    private val controller = StorageService.getRepository<Team>("TEAMS")

    val cache: MutableMap<String, Team> = mutableMapOf()

    fun load(){
        controller.repository.findAll().forEach {
            cache[it.identifier] = it
        }

        HCFPlugin.instance.logger.info("[Team Handler] Loaded successfully.")

        DTRHandler.load()
    }

    fun getTeam(id: String): Team? {
        return cache.getOrDefault(id.lowercase(), null)
    }

    fun getTeams(): MutableList<Team> {
        return cache.values.toMutableList()
    }

    fun create(team: Team){
        cache[team.identifier] = team
        save(team)
    }

    fun delete(team: Team){
        cache.remove(team.identifier)
        controller.repository.delete(team.identifier)
    }

    fun save(team: Team){
        controller.repository.save(team)
    }

    fun getTeamAtLocation(location: Location): Team? {
        return cache.values.firstOrNull {
            it.isInClaim(location)
        }
    }
}