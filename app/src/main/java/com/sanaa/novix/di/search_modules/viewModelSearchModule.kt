package com.sanaa.novix.di.search_modules

import com.sanaa.presentation.filter_bottomsheet.FilterViewModel
import com.sanaa.presentation.screen.SearchViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelSearchModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }

    viewModelOf(::FilterViewModel)
    viewModelOf(::SearchViewModel)
}