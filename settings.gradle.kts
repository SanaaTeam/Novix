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
include(":domain:authentication:repository")
include(":domain:movies:repository")
include(":domain:actors:repository")
include(":domain:series")
include(":domain:saved_content:repository")
include(":domain:search:repository")
include(":domain:user_profile:repository")
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
