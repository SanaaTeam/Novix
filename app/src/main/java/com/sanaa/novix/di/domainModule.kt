package com.sanaa.novix.di

import details.usecase.actor.GetActorDetailsUseCase
import details.usecase.actor.GetActorTopMoviesUseCase
import details.usecase.actor.GetActorTopTvSeriesUseCase
import details.usecase.actor.GetGalleryImagesUseCase
import details.usecase.actor.GetProfileImagesUseCase
import details.usecase.movie.GetMovieCastUseCase
import details.usecase.movie.GetMovieDetailsUseCase
import details.usecase.movie.GetMovieImagesUseCase
import details.usecase.movie.GetMovieTrailerUseCase
import details.usecase.movie.GetMoviesByCategory
import details.usecase.movie.GetReviewsByMovieId
import details.usecase.movie.GetSimilarMoviesByMovieId
import details.usecase.tv_series.GetEpisodeDetailsUseCase
import details.usecase.tv_series.GetEpisodeGuestsOfHonorUseCase
import details.usecase.tv_series.GetTvSeriesCastUseCase
import details.usecase.tv_series.GetTvSeriesDetailsUseCase
import details.usecase.tv_series.GetTvSeriesImagesUseCase
import details.usecase.tv_series.GetTvSeriesReviewsUseCase
import details.usecase.tv_series.GetTvSeriesSeasonDetailsUseCase
import details.usecase.tv_series.GetTvSeriesTrailerUseCase
import org.koin.dsl.module
import search.usecase.AddRecentViewedUseCase
import search.usecase.ClearRecentViewedUseCase
import search.usecase.ClearSearchHistoryUseCase
import search.usecase.GetRecentViewedUseCase
import search.usecase.GetSearchHistoryUseCase
import search.usecase.RemoveSearchHistoryUseCase
import search.usecase.SearchActorsUseCase
import search.usecase.SearchMoviesUseCase
import search.usecase.SearchTvSeriesUseCase

val domainModule = module {
    single { SearchMoviesUseCase(get(), get()) }
    single { SearchTvSeriesUseCase(get(), get()) }
    single { SearchActorsUseCase(get(), get()) }
    single { GetRecentViewedUseCase(get()) }
    single { GetSearchHistoryUseCase(get()) }
    single { ClearRecentViewedUseCase(get()) }
    single { ClearSearchHistoryUseCase(get()) }
    single { RemoveSearchHistoryUseCase(get()) }
    single { AddRecentViewedUseCase(get()) }
    single { RemoveSearchHistoryUseCase(get()) }

    // Actor
    single { GetActorDetailsUseCase(get()) }
    single { GetActorTopMoviesUseCase(get()) }
    single { GetActorTopTvSeriesUseCase(get()) }
    single { GetGalleryImagesUseCase(get()) }
    single { GetProfileImagesUseCase(get()) }

    // Movie
    single { GetMovieCastUseCase(get()) }
    single { GetMovieDetailsUseCase(get()) }
    single { GetMovieImagesUseCase(get()) }
    single { GetMoviesByCategory(get()) }
    single { GetReviewsByMovieId(get()) }
    single { GetMovieTrailerUseCase(get()) }
    single { GetSimilarMoviesByMovieId(get()) }

    // Series
    single { GetTvSeriesDetailsUseCase(get()) }
    single { GetTvSeriesImagesUseCase(get()) }
    single { GetTvSeriesTrailerUseCase(get()) }
    single { GetTvSeriesCastUseCase(get()) }
    single { GetTvSeriesSeasonDetailsUseCase(get()) }
    single { GetEpisodeDetailsUseCase(get()) }
    single { GetEpisodeGuestsOfHonorUseCase(get()) }
    single { GetTvSeriesReviewsUseCase(get()) }
}