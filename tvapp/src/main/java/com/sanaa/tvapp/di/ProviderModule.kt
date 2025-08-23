package com.sanaa.tvapp.di

import android.content.Context
import com.sanaa.vod.repository.service.VodStringProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import service.VodStringProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProviderModule {

    @Provides
    @Singleton
    fun provideVodStringProvider(@ApplicationContext context: Context): VodStringProvider {
        return VodStringProviderImpl(context)
    }
}