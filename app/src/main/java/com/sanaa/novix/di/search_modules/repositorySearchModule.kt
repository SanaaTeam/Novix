package com.sanaa.novix.di.search_modules

import com.sanaa.vod.repository.SearchHistoryRepositoryImpl
import com.sanaa.vod.repository.SearchRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import repository.HistoryRepository
import repository.SearchRepository

val repositorySearchModule = module {
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
    singleOf(::SearchHistoryRepositoryImpl) bind HistoryRepository::class
}