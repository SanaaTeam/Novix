package com.sanaa.tvapp.di.custom_list_modules

import com.sanaa.vod.cache.LocalSavedMovieDataSourceImpl
import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import com.sanaa.vod.repository.SavedListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.SavedListRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryCustomListModule {

    @Binds
    @Singleton
    abstract fun bindSavedListRepository(
        savedListRepositoryImpl: SavedListRepositoryImpl,
    ): SavedListRepository

    @Binds
    @Singleton
    abstract fun bindLocalSavedMovieDataSource(
        impl: LocalSavedMovieDataSourceImpl,
    ): LocalSavedMovieDataSource

}