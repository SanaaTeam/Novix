package com.sanaa.novix.di.search_modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import search.usecase.ManageRecentViewedUseCase
import search.usecase.ManageSearchHistoryUseCase
import search.usecase.SearchUseCase

val domainSearchModule = module {
    singleOf(::SearchUseCase)
    singleOf(::ManageRecentViewedUseCase)
    singleOf(::ManageSearchHistoryUseCase)
}