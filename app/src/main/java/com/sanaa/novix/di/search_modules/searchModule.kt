package com.sanaa.novix.di.search_modules

import org.koin.dsl.module

val searchModule = module {
    includes(
        domainSearchModule, repositorySearchModule, remoteSearchDataSource,
        localSearchDatabaseModule, viewModelSearchModule
    )
}