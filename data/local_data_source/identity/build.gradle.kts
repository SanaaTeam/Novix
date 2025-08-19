plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.novix.android.library)
    alias(libs.plugins.hilt.android)
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

    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.bundles.room.testing)

    implementation(libs.bundles.retrofit)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.protobuf.javalite)

    implementation(libs.slf4j.api)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.datastore)
}