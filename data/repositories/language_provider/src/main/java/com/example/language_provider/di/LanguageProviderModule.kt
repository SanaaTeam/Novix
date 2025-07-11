package com.example.language_provider.di

import com.example.language_provider.DeviceLanguageProvider
import repository.LanguageProvider
import org.koin.dsl.module

val languageProviderModule = module {
    single<LanguageProvider> {
        DeviceLanguageProvider()
    }
} 