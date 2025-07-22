package com.sanaa.novix.di.search_modules

import com.sanaa.vod.dataSource.remote.search.search.SearchRemoteDataSource
import com.sanaa.vod.search.SearchRemoteDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteSearchDataSource = module {
    singleOf(::SearchRemoteDataSourceImpl) bind SearchRemoteDataSource::class
}