package com.sanaa.actors.di

import com.sanaa.actors.ActorRemoteDataSourceImpl
import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
import org.koin.dsl.module

val remoteDetailsDataSource = module {
    single<ActorRemoteDataSource> { ActorRemoteDataSourceImpl(get(), get()) }
}