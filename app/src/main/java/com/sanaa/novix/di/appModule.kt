package com.sanaa.novix.di

import com.example.env_config.di.configModule
import com.sanaa.actors.di.remoteDetailsDataSource
import com.sanaa.movies.di.remoteMovieDetailsDataSource
import com.sanaa.search.di.networkModule
import com.sanaa.search.di.remoteDataSource
import com.sanaa.search.search_result.di.localDatabaseModule
import com.sanaa.series.di.remoteTvSeriesDataSourceModule
import org.koin.dsl.module

val appModule = module {
    includes(
        firebaseModule, loggingModule, configModule, repositoryModule, localDatabaseModule,
        networkModule, remoteDataSource, remoteDetailsDataSource, remoteMovieDetailsDataSource,
        remoteTvSeriesDataSourceModule,domainModule
    )
}