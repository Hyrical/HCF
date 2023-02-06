package org.hyrical.hcf.team

import org.hyrical.hcf.storage.StorageService
import org.hyrical.store.DataStoreController
import org.hyrical.store.connection.mongo.MongoConnection
import org.hyrical.store.connection.mongo.details.impl.NoAuthMongoDetails
import org.hyrical.store.type.StorageType
import java.util.UUID


object TeamService {

    private val controller = DataStoreController.of<Team>(
        StorageType.MONGO,
        StorageService.connection
    )

    val cache: MutableMap<String, Team> = mutableMapOf()

    init {
        controller.repository.findAll().forEach {
            cache[it.identifier] = it
        }
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

}