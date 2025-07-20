package com.sanaa.novix.di.details_modules

import org.koin.dsl.module

val detailsModule = module{
    includes(
        domainDetailsModule,repositoryDetailsModule,
        remoteDetailsDataSource,viewModelDetailsModule
    )
}