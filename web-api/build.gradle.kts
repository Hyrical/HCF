plugins {
    id(libs.plugins.ktor.plugin)
    application
}

application {
    mainClass.set("org.hyrical.webapi.HCFWebAPIKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.logging)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.junit)
    implementation(libs.store)
    implementation(libs.lamp.core)
    implementation(libs.lamp.cli)
}
