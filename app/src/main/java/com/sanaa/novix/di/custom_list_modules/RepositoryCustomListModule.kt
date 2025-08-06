package com.sanaa.novix.di.custom_list_modules

import com.sanaa.vod.repository.SavedListRepositoryImpl
import com.sanaa.vod.repository.SavedMovieStatusProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.SavedListRepository
import repository.SavedMovieStatusProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryCustomListModule {

    @Binds
    @Singleton
    abstract fun bindSavedListRepository(
        savedListRepositoryImpl: SavedListRepositoryImpl
    ): SavedListRepository

    @Binds
    @Singleton
    abstract fun bindSavedMovieStatusProvider(
        savedMovieStatusProviderImpl: SavedMovieStatusProviderImpl
    ): SavedMovieStatusProvider
}