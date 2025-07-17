package com.sanaa.novix.di

import com.sanaa.presentation.filter_bottomsheet.FilterViewModel
import com.sanaa.presentation.screen.SearchViewModel
import com.sanaa.presentation.screen.movie_details.MovieDetailsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { FilterViewModel(dispatcher = Dispatchers.IO) }
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
        viewModel {
            MovieDetailsViewModel(
                get(),
                get(),
                get(),
                get(),
                get()
            )
        }

}
