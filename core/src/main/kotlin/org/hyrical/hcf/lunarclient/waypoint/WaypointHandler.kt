package org.hyrical.hcf.lunarclient.waypoint

object WaypointHandler {

    // we shouldnt do this because waypoints change dynamlically tbh unless we need to store the id to remove or modifgy in the future
    val waypoints: MutableList<Waypoint> = mutableListOf()

    fun load(){
        waypoints//.add()
    }

}