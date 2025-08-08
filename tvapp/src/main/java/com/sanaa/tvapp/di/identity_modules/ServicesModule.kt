package com.sanaa.tvapp.di.identity_modules

import com.sanaa.identity.dataSoruce.dataStore.LocalUserDataSourceImpl
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServicesModule {
    @Binds
    @Singleton
    abstract fun bindLocalUserDataSource(
        localUserDataSourceImpl: LocalUserDataSourceImpl
    ): LocalUserDataSource
}