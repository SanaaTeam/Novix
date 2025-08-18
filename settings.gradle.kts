pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

includeBuild("build-logic")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Novix"
include(":app")
include(":data:repositories:vod")
include(":data:remote_data_source:vod")
include(":data:local_data_source:vod")
include(":feature:authentication:presentation")
include(":feature:authentication:api")
include(":feature:home:api")
include(":feature:home:presentation")
include(":feature:user_profile:api")
include(":feature:user_profile:presentation")
include(":feature:playlists:api")
include(":feature:playlists:presentation")
include(":feature:search:api")
include(":feature:search:presentation")
include(":feature:media_details:api")
include(":feature:media_details:presentation")
include(":domain:vod")
include(":image_viewer")
include(":design_system")
include(":domain:identity")

include(":data:local_data_source:identity")
include(":data:remote_data_source:identity")
include(":data:repositories:identity")

include(":feature:onboarding:api")
include(":feature:onboarding:presentation")
include(":feature:category:api")
include(":feature:category:presentation")
