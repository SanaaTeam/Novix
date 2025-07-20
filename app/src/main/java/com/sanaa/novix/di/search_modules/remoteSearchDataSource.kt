package com.sanaa.novix.di.search_modules

import com.sanaa.search.SearchRemoteDataSourceImpl
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteSearchDataSource = module {
    singleOf(::SearchRemoteDataSourceImpl) bind SearchRemoteDataSource::class
}