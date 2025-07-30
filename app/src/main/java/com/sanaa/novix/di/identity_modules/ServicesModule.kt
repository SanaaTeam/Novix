package com.sanaa.novix.di.identity_modules

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.dataSoruce.dataStore.PreferencesManagerImpl
import com.sanaa.novix.resourceProvider.StringProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import service.StringProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServicesModule {

    @Binds
    @Singleton
    abstract fun bindStringProvider(
        stringProviderImpl: StringProviderImpl
    ): StringProvider

    @Binds
    @Singleton
    abstract fun bindPreferencesManager(
        preferencesManagerImpl: PreferencesManagerImpl
    ): PreferencesManager
}