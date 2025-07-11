package com.sanaa.search.di

import com.sanaa.search.SearchRemoteDataSourceImpl
import com.sanaa.search.dataSource.SearchRemoteDataSource
import org.koin.dsl.module

val remoteDataSource = module {
    single<SearchRemoteDataSource> { SearchRemoteDataSourceImpl(get(), get()) }
}