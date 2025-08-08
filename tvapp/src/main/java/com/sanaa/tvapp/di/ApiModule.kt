package com.sanaa.tvapp.di

import com.sanaa.identity.dataSoruce.dataStore.PreferencesManagerImpl
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    @Singleton
    abstract fun bindPreferencesManager(
        preferencesManagerImpl: PreferencesManagerImpl
    ): PreferencesManager
}