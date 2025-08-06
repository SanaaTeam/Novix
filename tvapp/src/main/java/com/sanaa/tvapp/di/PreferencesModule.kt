package com.sanaa.tvapp.di

import com.sanaa.preferences.DeviceLanguageProvider
import com.sanaa.preferences.service.LanguageProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideLanguageProvider(): LanguageProvider =
        DeviceLanguageProvider()
}
