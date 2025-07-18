package com.sanaa.novix.di

import details.usecase.actor.GetActorDetailsUseCase
import details.usecase.actor.GetActorTopMoviesUseCase
import details.usecase.actor.GetActorTopTvSeriesUseCase
import details.usecase.actor.GetGalleryImagesUseCase
import details.usecase.actor.GetProfileImagesUseCase
import details.usecase.movie.GetMovieCastUseCase
import details.usecase.movie.GetMovieDetailsUseCase
import details.usecase.movie.GetMovieImagesUseCase
import details.usecase.movie.GetMoviesByCategory
import details.usecase.movie.GetReviewsByMovieId
import details.usecase.movie.GetSimilarMoviesByMovieId
import org.koin.core.module.dsl.singleOf
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
    singleOf(::SearchMoviesUseCase)
    singleOf(::SearchTvSeriesUseCase)
    singleOf(::SearchActorsUseCase)
    singleOf(::GetRecentViewedUseCase)
    singleOf(::GetSearchHistoryUseCase)
    singleOf(::ClearRecentViewedUseCase)
    singleOf(::ClearSearchHistoryUseCase)
    singleOf(::RemoveSearchHistoryUseCase)
    singleOf(::AddRecentViewedUseCase)
    singleOf(::RemoveSearchHistoryUseCase)

    singleOf(::GetActorDetailsUseCase)
    singleOf(::GetActorTopMoviesUseCase)
    singleOf(::GetActorTopTvSeriesUseCase)
    singleOf(::GetGalleryImagesUseCase)
    singleOf(::GetProfileImagesUseCase)

    singleOf(::GetMovieCastUseCase)
    singleOf(::GetMovieDetailsUseCase)
    singleOf(::GetMovieImagesUseCase)
    singleOf(::GetMoviesByCategory)
    singleOf(::GetReviewsByMovieId)
    singleOf(::GetSimilarMoviesByMovieId)
}