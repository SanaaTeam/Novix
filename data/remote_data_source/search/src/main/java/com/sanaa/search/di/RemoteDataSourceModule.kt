package com.sanaa.search.di

import com.sanaa.search.SearchRemoteDataSourceImpl
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteDataSource = module {
    singleOf(::SearchRemoteDataSourceImpl) bind SearchRemoteDataSource::class
}