package com.sanaa.novix.di

import org.koin.dsl.module

val appModule = module {
    includes(firebaseModule, loggingModule)
}