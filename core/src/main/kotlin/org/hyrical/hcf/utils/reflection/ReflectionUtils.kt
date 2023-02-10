package org.hyrical.hcf.utils.reflection

import java.lang.reflect.Field
import java.lang.reflect.Method

object ReflectionUtils {
    fun accessField(clazz: Class<*>, fieldName: String?): Field? {
        return try {
            val field = clazz.getDeclaredField(fieldName!!)
            field.isAccessible = true
            field
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            null
        }
    }

    fun accessMethod(clazz: Class<*>, methodName: String?, vararg vars: Class<*>?): Method? {
        return try {
            val method = clazz.getMethod(methodName!!, *vars)
            method.isAccessible = true
            method
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            null
        }
    }
}
