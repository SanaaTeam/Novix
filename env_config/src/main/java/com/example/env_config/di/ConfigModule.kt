package com.example.env_config.di

import com.example.env_config.DeviceLanguageProvider
import com.example.env_config.service.LanguageProvider
import org.koin.dsl.module

val configModule = module {
    single<LanguageProvider> { DeviceLanguageProvider() }
}