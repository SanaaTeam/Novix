package com.sanaa.tvapp.di.identity_modules

import com.sanaa.identity.dataSoruce.dataStore.LocalUserDataSourceImpl
import com.sanaa.identity.dataSoruce.dataStore.LocalUserPreferenceImpl
import com.sanaa.identity.dataSoruce.dataStore.PreferencesManagerImpl
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserDataSource
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserPreferenceDataSource
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.tvapp.resourceProvider.StringProviderImpl
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

    @Binds
    @Singleton
    abstract fun bindLocalUserDataSource(
        localUserDataSourceImpl: LocalUserDataSourceImpl
    ): LocalUserDataSource

    @Binds
    @Singleton
    abstract fun bindLocalUserPreferenceDataSource(
        impl: LocalUserPreferenceImpl
    ): LocalUserPreferenceDataSource
}