dependencies {
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    implementation("com.github.Nopock:Store:v4.21")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("com.eatthepath:fast-uuid:0.2.0")
    implementation("org.mongodb:mongo-java-driver:3.12.11")
    implementation("com.github.ThatKawaiiSam:Assemble:7b446313ba")
    implementation("com.github.cryptomorin:XSeries:9.3.0") { isTransitive = false }

    implementation("com.github.mkremins:fanciful:ed870f3a19")

    // Include all libs sub folders too
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("**/*.jar"))))

    implementation(project(":core-api"))

    implementation("com.github.LunarClient:BukkitAPI-NetHandler:fab9915b0d")
}