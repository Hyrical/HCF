dependencies {
    implementation(libs.acf.paper)

    implementation(libs.store)
    implementation(libs.kotlin.reflect)

    implementation(libs.apache.commons)
    implementation(libs.reflections)
    implementation(libs.google.guava)
    implementation(libs.apache.commons.collection)
    implementation(libs.fast.uuid)
    implementation(libs.mongo.java.driver)
    implementation(libs.xseries) { isTransitive = false }

    implementation(libs.fanciful)

    // Include all libs sub folders too
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("**/*.jar"))))

    implementation(project(":core-api"))

    implementation(libs.lunar.client.api)
}
