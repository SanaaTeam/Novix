package com.sanaa.actors.di

import com.sanaa.actors.ActorRemoteDataSourceImpl
import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteDetailsDataSource = module {
    singleOf(::ActorRemoteDataSourceImpl) bind ActorRemoteDataSource::class
}