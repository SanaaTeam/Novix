package com.sanaa.tvapp.di

import com.sanaa.vod.repository.service.VodStringProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import service.VodStringProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServicesModule {

    @Binds
    @Singleton
    abstract fun bindVodStringProvider(
        vodStringProviderImpl: VodStringProviderImpl
    ): VodStringProvider
}