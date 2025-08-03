package com.sanaa.novix.di.identity_modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.sanaa.identity.dataSoruce.dataStore.LocalUserDataSourceImpl
import com.sanaa.identity.dataSoruce.dataStore.PreferencesManagerImpl
import com.sanaa.identity.dataSoruce.dataStore.mapper.UserSerializer
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserDataSource
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserPreferenceDataSource
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.dataSource.dataStore.LocalUserPreferenceImpl
import com.sanaa.identity.proto.User
import com.sanaa.novix.resourceProvider.StringProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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