package com.sanaa.search.data.di

import com.sanaa.search.data.repository.SearchRepository
import com.sanaa.search.data.repository.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(
            localDataSource = get()
        )
    }
} 