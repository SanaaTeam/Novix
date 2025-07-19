package com.sanaa.novix.di

import com.sanaa.presentation.filter_bottomsheet.FilterViewModel
import com.sanaa.presentation.screen.SearchViewModel
import com.sanaa.presentation.screen.actor.ActorViewModel
import com.sanaa.presentation.screen.episode_details.EpisodeDetailsScreenViewModel
import com.sanaa.presentation.screen.movie_categories.MovieCategoriesViewModel
import com.sanaa.presentation.screen.movie_details.MovieDetailsViewModel
import com.sanaa.presentation.screen.review.ReviewViewModel
import com.sanaa.presentation.screen.series.SeriesViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    single { Dispatchers.IO }
    viewModelOf(::FilterViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::SeriesViewModel)
    viewModelOf(::EpisodeDetailsScreenViewModel)
    viewModelOf(::ActorViewModel)
    viewModelOf(::ReviewViewModel)
    viewModelOf(::MovieCategoriesViewModel)
}
