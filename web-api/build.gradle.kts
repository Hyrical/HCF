val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    id("io.ktor.plugin") version "2.2.3"
    application
}

application {
    mainClass.set("org.hyrical.webapi.HCFWebAPIKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("com.github.Nopock:Store:v4.21")
    implementation("com.github.Revxrsal.Lamp:common:3.1.3")
    implementation("com.github.Revxrsal.Lamp:cli:3.1.3")
}