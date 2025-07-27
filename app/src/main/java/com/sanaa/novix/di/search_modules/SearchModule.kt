package com.sanaa.novix.di.search_modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        DomainSearchModule::class,
        LocalSearchDatabaseModule::class,
        LocalSearchDataSourceModule::class,
        RemoteSearchDataSourceModule::class,
        RepositorySearchModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object SearchModule
