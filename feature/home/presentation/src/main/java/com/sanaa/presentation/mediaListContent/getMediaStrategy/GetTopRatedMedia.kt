package com.sanaa.presentation.mediaListContent.getMediaStrategy

import android.content.Context
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.model.GenreUiState
import com.sanaa.presentation.model.MediaItem
import com.sanaa.presentation.model.MediaType
import com.sanaa.presentation.model.toState
import entity.Movie
import entity.TvSeries
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase

class GetTopRatedMedia(
    private val context: Context,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val manageMovieUseCase: ManageMovieUseCase
) : GetMediaStrategy {
    override fun getMediaSectionTitle(): String {
        return context.getString(R.string.top_rated)
    }

    override suspend fun getMediaList(page: Int, genreId: Int?): List<MediaItem> {
        return manageMovieUseCase.getTopRatedMovies(page, genreId).map { it.toState() }
            .plus(manageTvSeriesUseCase.getTopRatedTvSeries(page, genreId).map { it.toState() })
            .sortedByDescending { it.rating }
    }

    override suspend fun getMediaGenreList(): List<GenreUiState> {
        return manageMovieUseCase.getMovieGenres()
            .plus(manageTvSeriesUseCase.getSeriesGenres())
            .distinctBy { it.id }
            .sortedBy { it.name }
            .toState()
    }
}

fun Movie.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = imdbRating,
    mediaType = MediaType.MOVIE
)

fun TvSeries.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = imdbRating,
    mediaType = MediaType.TV_SHOW
)