package com.sanaa.novix.di

import com.sanaa.search.data.di.localDatabaseModule
import com.sanaa.search.data.di.repositoryModule
import com.sanaa.search.domain.di.domainModule
import com.sanaa.presentation.di.presentationModule
import org.koin.dsl.module

val appModule = module {
    includes(
        firebaseModule, 
        loggingModule, 
        localDatabaseModule,
        repositoryModule,
        domainModule,
        presentationModule
    )
}