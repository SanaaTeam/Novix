package com.example.preferences.di

import com.example.preferences.DeviceLanguageProvider
import com.example.preferences.GenreLocalizerImpl
import com.example.preferences.service.GenreLocalizer
import com.example.preferences.service.LanguageProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {
    single<LanguageProvider> { DeviceLanguageProvider() }
    single<GenreLocalizer> { GenreLocalizerImpl(androidContext()) }
}