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
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Novix"
include(":app")
include(":feature:authentication:presentation")
include(":feature:authentication:api")
include(":feature:home:api")
include(":feature:home:presentation")
include(":feature:user_profile:api")
include(":feature:user_profile:presentation")
include(":feature:saved_content:api")
include(":feature:saved_content:presentation")
include(":feature:playlists:api")
include(":feature:playlists:presentation")
include(":feature:search:api")
include(":feature:search:presentation")
include(":feature:media_details:api")
include(":feature:media_details:presentation")
include(":feature:onboarding:api")
include(":feature:onboarding:presentation")
include(":domain:authentication")
include(":domain:saved_content")
include(":domain:vod")
include(":domain:user_profile")
include(":data:remote_data_source:actors")
include(":data:remote_data_source:authentication")
include(":data:remote_data_source:movies")
include(":data:remote_data_source:saved_content")
include(":data:remote_data_source:search")
include(":data:remote_data_source:series")
include(":data:remote_data_source:user_profile")
include(":data:local_data_source:actors")
include(":data:local_data_source:authentication")
include(":data:local_data_source:movies")
include(":data:local_data_source:saved_content")
include(":data:local_data_source:search")
include(":data:local_data_source:series")
include(":data:local_data_source:user_profile")
include(":data:repositories:actors")
include(":data:repositories:authentication")
include(":data:repositories:movies")
include(":data:repositories:saved_content")
include(":data:repositories:search")
include(":data:repositories:series")
include(":data:repositories:user_profile")
include(":image_viewer")
include(":design_system")
include(":preferences")
