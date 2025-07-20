package com.sanaa.novix.di.search_modules

import com.sanaa.search.repository.SearchHistoryRepositoryImpl
import com.sanaa.search.repository.SearchRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import search.repository.SearchHistoryRepository
import search.repository.SearchRepository

val repositorySearchModule = module {
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
    singleOf(::SearchRepositoryImpl)
    singleOf(::SearchHistoryRepositoryImpl) bind SearchHistoryRepository::class
}