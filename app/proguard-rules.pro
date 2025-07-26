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
-dontwarn com.sanaa.presentation.navigation.AuthNavHostKt
-dontwarn com.sanaa.presentation.navigation.DetailsNavHostKt
-dontwarn com.sanaa.presentation.navigation.StartRoute
-dontwarn com.sanaa.presentation.screen.SearchViewModel
-dontwarn com.sanaa.presentation.screen.actor.ActorViewModel
-dontwarn com.sanaa.presentation.screen.episodeDetails.EpisodeDetailsScreenViewModel
-dontwarn com.sanaa.presentation.screen.genreMovies.MovieCategoriesViewModel
-dontwarn com.sanaa.presentation.screen.movieDetails.MovieDetailsViewModel
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
-dontwarn com.sanaa.series.repository.TvSeriesRepositoryImpl
-dontwarn com.sanaa.series.dataSource.remote.RemoteTvSeriesDataSource
-dontwarn com.sanaa.series.di.RemoteTvSeriesDataSourceModuleKt
-dontwarn com.sanaa.api.MediaDetailsApi
-dontwarn com.sanaa.api.SearchFeatureApi
-dontwarn com.sanaa.api.StartRoute
-dontwarn com.sanaa.presentation.api.SearchFeatureApiImpl
-dontwarn com.sanaa.presentation.navigation.MediaDetailsApiImpl


# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode generic signatures are stripped for classes that are not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-dontwarn com.sanaa.presentation.api.MediaDetailsApiImpl