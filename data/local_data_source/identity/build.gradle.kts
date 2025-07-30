plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.novix.android.library)
    id("com.google.protobuf") version "0.9.4" // Add protobuf plugin

}
android {
    namespace = "com.sanaa.identity"
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite") // Use lite for Android
                }
            }
        }
    }
}

dependencies {
    implementation(projects.data.repositories.identity)
    implementation(projects.domain.identity)
    implementation(projects.preferences)

    // Room dependencies
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.bundles.room.testing)

    implementation(libs.retrofit)
    implementation(libs.androidx.datastore.preferences)

    // WorkManager for background cache cleanup
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.protobuf.javalite)

    // Koin dependencies
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(libs.slf4j.api)

    implementation(libs.androidx.datastore)

}