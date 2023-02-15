package org.hyrical.hcf.team.claim.cuboid

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.Bukkit
import org.bukkit.Location
import org.hyrical.hcf.utils.getLang
import java.lang.reflect.Type

object CuboidSerializer : TypeAdapter<Cuboid>() {

    @JvmStatic
    fun serialize(cuboid: Cuboid): JsonObject {
        val json = JsonObject()
        json.addProperty("worldName", cuboid.world.name)
        json.addProperty("lowerX", cuboid.lowerX)
        json.addProperty("lowerY", cuboid.lowerY)
        json.addProperty("lowerZ", cuboid.lowerZ)
        json.addProperty("upperX", cuboid.upperX)
        json.addProperty("upperY", cuboid.upperY)
        json.addProperty("upperZ", cuboid.upperZ)
        return json
    }

    @JvmStatic
    fun deserialize(json: JsonElement): Cuboid {

    }

    override fun write(p0: JsonWriter?, p1: Cuboid?) {

    }

    override fun read(json2: JsonReader?): Cuboid {
        val json = json2!!
        val world = Bukkit.getWorld(json.) ?: throw IllegalStateException("World ${json["worldName"].asString} is not loaded")

        val lowerCorner = Location(
            world,
            json["lowerX"].asInt.toDouble(),
            json["lowerY"].asInt.toDouble(),
            json["lowerZ"].asInt.toDouble()
        )

        val upperCorner = Location(
            world,
            json["upperX"].asInt.toDouble(),
            json["upperY"].asInt.toDouble(),
            json["upperZ"].asInt.toDouble()
        )

        return Cuboid(lowerCorner, upperCorner)    }

}