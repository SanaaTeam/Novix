package com.sanaa.novix.di

import com.sanaa.presentation.screen.actor.ActorViewModel
import details.usecase.actor.GetActorDetailsUseCase
import details.usecase.actor.GetActorTopMoviesUseCase
import details.usecase.actor.GetActorTopTvSeriesUseCase
import details.usecase.actor.GetGalleryImagesUseCase
import details.usecase.actor.GetProfileImagesUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import details.usecase.movie.GetMovieCastUseCase
import details.usecase.movie.GetMovieDetailsUseCase
import details.usecase.movie.GetMovieImagesUseCase
import details.usecase.movie.GetMoviesByCategory
import details.usecase.movie.GetReviewsByMovieId
import details.usecase.movie.GetSimilarMoviesByMovieId
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

    single { GetActorDetailsUseCase(get()) }
    single { GetActorTopMoviesUseCase(get()) }
    single { GetActorTopTvSeriesUseCase(get()) }
    single { GetGalleryImagesUseCase(get()) }
    single { GetProfileImagesUseCase(get()) }

    single { GetMovieCastUseCase(get()) }
    single { GetMovieDetailsUseCase(get()) }
    single { GetMovieImagesUseCase(get()) }
    single { GetMoviesByCategory(get()) }
    single { GetReviewsByMovieId(get()) }
    single { GetSimilarMoviesByMovieId(get()) }

    viewModelOf( ::ActorViewModel )
    viewModel { (actorId: Int) ->
        ActorViewModel(actorId = actorId, get(), get(), get(), get(), get())
    }
    single { GetMovieTrailerUseCase(get()) }
}