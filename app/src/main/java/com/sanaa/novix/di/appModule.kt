package com.sanaa.novix.di

import com.example.language_provider.di.languageProviderModule
import com.sanaa.search.search_result.di.localDatabaseModule
import com.sanaa.search.search_result.di.repositoryModule
import com.sanaa.search.domain.di.domainModule
import com.sanaa.presentation.di.presentationModule
import org.koin.dsl.module

val appModule = module {
    includes(
        firebaseModule, 
        loggingModule, 
        languageProviderModule,
        localDatabaseModule,
        repositoryModule,
        domainModule,
        presentationModule
    )
}