package com.sanaa.novix.di

import com.example.env_config.di.configModule
import org.koin.dsl.module

val appModule = module {
    includes(firebaseModule, loggingModule, configModule)
}