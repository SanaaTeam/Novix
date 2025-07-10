package com.sanaa.search.di

import com.sanaa.search.SearchRemoteDataSourceImpl
import org.koin.dsl.module

val remoteDataSource =module {
    single { SearchRemoteDataSourceImpl(get()) }
}