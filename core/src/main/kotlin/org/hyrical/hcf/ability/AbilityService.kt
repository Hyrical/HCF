package org.hyrical.hcf.ability

object AbilityService
{
    private val abilities = hashMapOf<String, Ability>()

    fun loadAll() {}

    fun byName(name: String) : Ability?
    {
        return abilities.getOrDefault(name.lowercase(), null)
    }

}