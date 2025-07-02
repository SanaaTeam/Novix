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
include(":data:authentication:repository")
include(":data:movies:repository")
include(":data:series:repository")
include(":data:search:repository")
include(":data:saved_content:repository")
include(":data:user_profile:repository")
include(":data:remote_data_source")
include(":data:local_data_source")
include(":data:actors:data_source:remote")
include(":data:actors:repository")
include(":data:authentication:data_source:remote")
include(":data:movies:data_source:remote")
include(":data:saved_content:data_source:remote")
include(":data:search:data_source:remote")
include(":data:series:data_source:remote")
include(":data:user_profile:data_source:remote")
include(":data:actors:data_source:local")
include(":data:authentication:data_source:local")
include(":data:movies:data_source:local")
include(":data:saved_content:data_source:local")
include(":data:search:data_source:local")
include(":data:series:data_source:local")
include(":data:user_profile:data_source:local")
include(":domain:authentication:repository")
include(":domain:movies:repository")
include(":domain:actors:repository")
include(":domain:series")
include(":domain:saved_content:repository")
include(":domain:search:repository")
include(":domain:user_profile:repository")
