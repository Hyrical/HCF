import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("maven-publish")
}

group = "org.hyrical"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://jitpack.io")
    maven("https://repo.lucko.me/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    implementation("com.github.Nopock:Store:v4.21")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")

    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("org.reflections:reflections:0.9.11")
    implementation("com.google.guava:guava:23.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("com.eatthepath:fast-uuid:0.2.0")
    implementation("org.mongodb:mongo-java-driver:3.12.11")
    implementation("com.github.ThatKawaiiSam:Assemble:7b446313ba")
    implementation("com.github.cryptomorin:XSeries:9.2.0") { isTransitive = false }

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
