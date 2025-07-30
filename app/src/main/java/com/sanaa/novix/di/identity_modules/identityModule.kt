package com.sanaa.novix.di.identity_modules

import org.koin.dsl.module

val identityModule = module {
    includes(
        domainIdentityModule,
        repositoryIdentityModule,
        remoteIdentityDataSource,
        servicesModule,
        viewModelIdentityModule
    )
}