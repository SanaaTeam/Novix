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
include(":domain:actors")
include(":domain:authentication")
include(":domain:movies")
include(":domain:series")
include(":domain:search")
include(":domain:user_profile")
include(":data_source:authentication:repository")
include(":data_source:movies:repository")
include(":data_source:series:repository")
include(":data_source:actors:repository")
include(":data_source:search:repository")
include(":domain:saved_content")
include(":data_source:saved_content:repository")
include(":data_source:user_profile:repository")
include(":data_source:remote_data_source")
include(":data_source:local_data_source")
