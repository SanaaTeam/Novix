package com.sanaa.novix.di.search_modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import usecase.search.ManageRecentViewedUseCase
import usecase.history.ManageHistoryUseCase
import usecase.search.SearchUseCase

val domainSearchModule = module {
    singleOf(::SearchUseCase)
    singleOf(::ManageRecentViewedUseCase)
    singleOf(::ManageHistoryUseCase)
}