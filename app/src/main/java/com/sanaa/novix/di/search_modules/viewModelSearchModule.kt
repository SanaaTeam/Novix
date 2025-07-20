package com.sanaa.novix.di.search_modules

import com.sanaa.presentation.filter_bottomsheet.FilterViewModel
import com.sanaa.presentation.screen.SearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelSearchModule = module {
    viewModel {
        FilterViewModel(
            dispatcher = Dispatchers.IO,
            genreLocalizer = get()
        )
    }
    viewModel {
        SearchViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            dispatcher = Dispatchers.IO,
        )
    }
    single { Dispatchers.IO }
}