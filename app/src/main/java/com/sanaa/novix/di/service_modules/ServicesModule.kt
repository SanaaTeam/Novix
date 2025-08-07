package com.sanaa.novix.di.service_modules

import com.sanaa.identity.dataSoruce.dataStore.LocalUserDataSourceImpl
import com.sanaa.identity.dataSoruce.dataStore.LocalUserPreferenceImpl
import com.sanaa.identity.dataSoruce.dataStore.PreferencesManagerImpl
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserDataSource
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserPreferenceDataSource
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.repository.service.IdentityStringProviderImpl
import com.sanaa.vod.repository.service.VodStringProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import service.IdentityStringProvider
import service.VodStringProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServicesModule {

    @Binds
    @Singleton
    abstract fun bindIdentityStringProvider(
        identityStringProviderImpl: IdentityStringProviderImpl
    ): IdentityStringProvider

    @Binds
    @Singleton
    abstract fun bindVodStringProvider(
        vodStringProviderImpl: VodStringProviderImpl
    ): VodStringProvider

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
        localUserPreferenceImplImpl: LocalUserPreferenceImpl
    ): LocalUserPreferenceDataSource
}