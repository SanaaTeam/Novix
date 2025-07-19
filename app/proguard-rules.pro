# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn org.slf4j.impl.StaticLoggerBinder
-assumenosideeffects class androidx.compose.ui.tooling.preview.Preview { *; }
-dontwarn com.example.preferences.di.PreferencesModuleKt
-dontwarn com.example.preferences.service.GenreLocalizer
-dontwarn com.example.preferences.service.LanguageProvider
-dontwarn com.sanaa.actors.ActorRemoteDataSourceImpl
-dontwarn com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
-dontwarn com.sanaa.actors.di.RemoteDetailsDataSourceModuleKt
-dontwarn com.sanaa.actors.repository.ActorRepositoryImpl
-dontwarn com.sanaa.movies.MovieDetailsRemoteDataSourceImpl
-dontwarn com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
-dontwarn com.sanaa.movies.di.RemoteDataSourceModuleKt
-dontwarn com.sanaa.movies.repository.MovieRepositoryImpl
-dontwarn com.sanaa.presentation.filter_bottomsheet.FilterViewModel
-dontwarn com.sanaa.presentation.model.MediaTypeUiModel
-dontwarn com.sanaa.presentation.navigation.DetailsNavHostKt
-dontwarn com.sanaa.presentation.navigation.StartRoute
-dontwarn com.sanaa.presentation.screen.SearchViewModel
-dontwarn com.sanaa.presentation.screen.actor.ActorViewModel
-dontwarn com.sanaa.presentation.screen.episode_details.EpisodeDetailsScreenViewModel
-dontwarn com.sanaa.presentation.screen.movie_categories.MovieCategoriesViewModel
-dontwarn com.sanaa.presentation.screen.movie_details.MovieDetailsViewModel
-dontwarn com.sanaa.presentation.screen.review.ReviewViewModel
-dontwarn com.sanaa.presentation.screen.series.SeriesViewModel
-dontwarn com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
-dontwarn com.sanaa.search.dataSource.local.LocalSearchHistoryDataSource
-dontwarn com.sanaa.search.dataSource.remote.SearchRemoteDataSource
-dontwarn com.sanaa.search.di.NetworkModuleKt
-dontwarn com.sanaa.search.di.RemoteDataSourceModuleKt
-dontwarn com.sanaa.search.repository.SearchHistoryRepositoryImpl
-dontwarn com.sanaa.search.repository.SearchRepositoryImpl
-dontwarn com.sanaa.search.search_history.LocalSearchHistoryDataSourceImpl
-dontwarn com.sanaa.search.search_history.dao.QueryDao
-dontwarn com.sanaa.search.search_history.dao.RecentViewedDao
-dontwarn com.sanaa.search.search_result.di.LocalDatabaseModuleKt
-dontwarn com.sanaa.series.TvSeriesRepositoryImpl
-dontwarn com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
-dontwarn com.sanaa.series.di.RemoteTvSeriesDataSourceModuleKt