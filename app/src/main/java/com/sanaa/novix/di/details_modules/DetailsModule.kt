package com.sanaa.novix.di.details_modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        DomainDetailsModule::class,
        RemoteDetailsDataSourceModule::class,
        RepositoryDetailsModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object DetailsModule
