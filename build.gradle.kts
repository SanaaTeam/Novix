// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kover)
}
subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    kover {
        reports {
            filters {
                excludes {
                    classes(
                        "*.R",
                        "*.R_*",
                        "*.BuildConfig*",
                        "*.Manifest*",
                        "com.sanaa.novix.ui.theme.*",
                        "*.ComposableSingletons*",
                        "*.MainActivity*"
                    )
                }
            }



            verify {
                rule {
                    bound {
                        minValue = 80
                    }
                }
            }
        }
    }
}