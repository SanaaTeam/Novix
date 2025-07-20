package com.sanaa.novix.di

import com.sanaa.preferences.DeviceLanguageProvider
import com.sanaa.preferences.GenreLocalizerImpl
import com.sanaa.preferences.service.GenreLocalizer
import com.sanaa.preferences.service.LanguageProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {
    single<LanguageProvider> { DeviceLanguageProvider() }
    single<GenreLocalizer> { GenreLocalizerImpl(androidContext()) }
}