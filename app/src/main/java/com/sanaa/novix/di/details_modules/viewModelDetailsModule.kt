package com.sanaa.novix.di.details_modules

import com.sanaa.presentation.screen.actor.ActorViewModel
import com.sanaa.presentation.screen.episode_details.EpisodeDetailsScreenViewModel
import com.sanaa.presentation.screen.movie_details.MovieDetailsViewModel
import com.sanaa.presentation.screen.genreMovies.GenreMoviesViewModel
import com.sanaa.presentation.screen.review.ReviewViewModel
import com.sanaa.presentation.screen.series.SeriesViewModel
import com.sanaa.presentation.screen.genreTvShows.GenreTvShowsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelDetailsModule = module {
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::SeriesViewModel)
    viewModelOf(::EpisodeDetailsScreenViewModel)
    viewModelOf(::ActorViewModel)
    viewModelOf(::ReviewViewModel)
    viewModelOf(::GenreMoviesViewModel)
    viewModelOf(::GenreTvShowsViewModel)

}