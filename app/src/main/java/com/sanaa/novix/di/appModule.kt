package com.sanaa.novix.di

import com.sanaa.novix.di.details_modules.detailsModule
import com.sanaa.novix.di.search_modules.searchModule
import org.koin.dsl.module

val appModule = module {
    includes(
        firebaseModule, loggingModule, navigationModule, networkModule, preferencesModule,
        detailsModule, searchModule
    )
}