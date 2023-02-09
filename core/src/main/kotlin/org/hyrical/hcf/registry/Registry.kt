package org.hyrical.hcf.registry

interface Registry {

    fun getAnnotation(): Class<out Annotation>

    fun getParent(): Class<*>

    fun register(t: Any)
}