package org.hyrical.hcf.utils.reflection

import org.bukkit.plugin.java.JavaPlugin
import org.reflections.util.ClasspathHelper
import org.reflections.vfs.Vfs

object ClassScanner {

    fun getClassesInPackage(
        plugin: JavaPlugin,
        packageName: String,
        annotation: Class<out Annotation>,
        filter: Class<*>
    ): List<Class<*>> {
        val classes = mutableListOf<Class<*>>()
        for (url in ClasspathHelper.forClassLoader(
            ClasspathHelper.contextClassLoader(),
            ClasspathHelper.staticClassLoader(),
            plugin.javaClass.classLoader
        )) {
            val dir = Vfs.fromURL(url)
            try {
                for (file in dir.files) {
                    val name = file.relativePath.replace("/", ".").replace(".class", "").replace("es", "")
                    if (name.startsWith(packageName)) {
                        classes.add(Class.forName(name))
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                dir.close()
            }
        }

        return classes.filter { it.isAnnotationPresent(annotation) && it.isAssignableFrom(filter) }
    }

    fun getClassesInPackage(
        plugin: JavaPlugin,
        packageName: String,
    ): List<Class<*>> {
        val classes = mutableListOf<Class<*>>()
        for (url in ClasspathHelper.forClassLoader(
            ClasspathHelper.contextClassLoader(),
            ClasspathHelper.staticClassLoader(),
            plugin.javaClass.classLoader
        )) {
            val dir = Vfs.fromURL(url)
            try {
                for (file in dir.files) {
                    val name = file.relativePath.replace("/", ".").replace(".class", "").replace("es", "")
                    if (name.startsWith(packageName)) {
                        classes.add(Class.forName(name))
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                dir.close()
            }
        }

        return classes
    }
}