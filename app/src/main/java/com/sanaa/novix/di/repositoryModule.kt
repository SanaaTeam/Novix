package com.sanaa.novix.di

import com.sanaa.search.dataSource.local.LocalSearchHistoryDataSource
import com.sanaa.search.repository.SearchHistoryRepositoryImpl
import com.sanaa.search.repository.SearchPagingRepositoryImpl
import com.sanaa.search.repository.SearchRepositoryImpl
import com.sanaa.search.search_history.LocalSearchHistoryDataSourceImpl
import org.koin.dsl.module
import repository.SearchHistoryRepository
import repository.SearchPagingRepository
import repository.SearchRepository

val repositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get(), get(), get()) }
    single { SearchRepositoryImpl(get(), get(), get()) }
    single<SearchPagingRepository> { SearchPagingRepositoryImpl(get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
    single<LocalSearchHistoryDataSource> { LocalSearchHistoryDataSourceImpl(get(), get()) }
}
