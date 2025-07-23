package com.sanaa.presentation.fake

import usecase.search.search_param.SearchActorOutput
import usecase.search.search_param.SearchMovieOutput
import usecase.search.search_param.SearchTvSeriesOutput

object FakeData {
    val actorOutputs: List<SearchActorOutput> = listOf(
        SearchActorOutput(1, "Tom Hanks", "image.com"),
        SearchActorOutput(2, "Tom Holland", "image.com")
    )

    val moviesOutput: List<SearchMovieOutput> = listOf(
        SearchMovieOutput(1, "IronMan1", "image.com"),
        SearchMovieOutput(2, "IronMan2", "image.com")
    )

    val tvShowsOutput: List<SearchTvSeriesOutput> = listOf(
        SearchTvSeriesOutput(1, "Tom And Jerry", "image.com"),
        SearchTvSeriesOutput(2, "Friends", "image.com")
    )
}