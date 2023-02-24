import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadowjar)
}

allprojects {
    group = "org.hyrical"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://jitpack.io")
        maven("https://repo.lucko.me/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
    }
}
