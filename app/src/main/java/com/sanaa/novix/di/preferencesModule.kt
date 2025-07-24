package com.sanaa.novix.di

import com.sanaa.preferences.DeviceLanguageProvider
import com.sanaa.preferences.service.LanguageProvider
import org.koin.dsl.module

val preferencesModule = module {
    single<LanguageProvider> { DeviceLanguageProvider() }
}