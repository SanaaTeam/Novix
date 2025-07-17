package com.sanaa.novix.di

import com.example.preferences.di.preferencesModule
import com.sanaa.actors.di.remoteDetailsDataSource
import com.sanaa.movies.di.remoteMovieDetailsDataSource
import com.sanaa.search.di.networkModule
import com.sanaa.search.di.remoteDataSource
import com.sanaa.search.search_result.di.localDatabaseModule
import org.koin.dsl.module

val appModule = module {
    includes(
        firebaseModule, loggingModule, repositoryModule, localDatabaseModule,
        networkModule, remoteDataSource, remoteDetailsDataSource,remoteMovieDetailsDataSource
    )
}