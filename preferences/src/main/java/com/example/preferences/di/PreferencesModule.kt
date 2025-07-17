package com.example.preferences.di

import com.example.preferences.DeviceLanguageProvider
import com.example.preferences.service.LanguageProvider
import org.koin.dsl.module

val preferencesModule = module {
    single<LanguageProvider> { DeviceLanguageProvider() }
}