package com.sanaa.vod.repository.mapper.custom_list

import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto
import entity.Movie
import kotlinx.datetime.LocalDate
import usecase.custom_list.custom_list_param.SavedList

fun SavedListDto.toEntity() = SavedList(id, title, itemCount)

fun SavedItemDto.toEntity() = Movie(
    id = id,
    posterImageUrl = posterPath ?: "",
    title = title ?: originalTitle ?: "Untitled",
    genres = emptyList(),
    imdbRating = voteAverage,
    duration = null,
    releaseDate = LocalDate.parse(releaseDate ?: "2000-01-01"),
    overview = overview ?: "",
    trailerUrl = null,
    rating = null
)