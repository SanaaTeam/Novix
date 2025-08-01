package com.sanaa.novix.di.details_modules

import com.sanaa.presentation.screen.actor.ActorViewModel
import com.sanaa.presentation.screen.celebritiesScreen.CelebritiesViewModel
import com.sanaa.presentation.screen.episodeDetails.EpisodeDetailsScreenViewModel
import com.sanaa.presentation.screen.genreMovies.GenreMoviesViewModel
import com.sanaa.presentation.screen.genreTvShows.GenreTvShowsViewModel
import com.sanaa.presentation.screen.homeScreen.HomeScreenViewModel
import com.sanaa.presentation.screen.mediaTabScreen.continueWatchingScreen.ContinueWatchingMediaScreenViewModel
import com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen.TopRatedMediaScreenViewModel
import com.sanaa.presentation.screen.movieDetails.MovieDetailsViewModel
import com.sanaa.presentation.screen.review.ReviewViewModel
import com.sanaa.presentation.screen.series.SeriesViewModel
import com.sanaa.presentation.screen.trendingMediaScreen.trendingMoviesScreen.TrendingMoviesScreenViewModel
import com.sanaa.presentation.screen.trendingMediaScreen.trendingTvShowScreen.TrendingTvShowsScreenViewModel
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
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::TrendingMoviesScreenViewModel)
    viewModelOf(::TrendingTvShowsScreenViewModel)
    viewModelOf(::CelebritiesViewModel)
    viewModelOf(::TopRatedMediaScreenViewModel)
    viewModelOf(::ContinueWatchingMediaScreenViewModel)
}