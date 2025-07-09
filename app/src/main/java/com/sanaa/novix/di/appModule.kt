package com.sanaa.novix.di

import com.sanaa.search.data.di.localDatabaseModule
import org.koin.dsl.module

val appModule = module {
    includes(firebaseModule, loggingModule, localDatabaseModule)
}