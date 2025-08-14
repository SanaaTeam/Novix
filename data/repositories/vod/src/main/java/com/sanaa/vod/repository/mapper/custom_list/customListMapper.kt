package com.sanaa.vod.repository.mapper.custom_list

import com.sanaa.vod.dataSource.local.cache.dto.SavedListLocalDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto
import com.sanaa.vod.repository.mapper.media.getFullImageUrl
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Movie
import usecase.custom_list.custom_list_param.SavedList

fun SavedListDto.toEntity() = SavedList(id, title, itemCount)

fun SavedItemDto.toEntity() = Movie(
    id = id,
    posterImageUrl = getFullImageUrl(posterPath),
    title = title ?: originalTitle ?: "Untitled",
    genres = emptyList(),
    imdbRating = voteAverage,
    duration = null,
    releaseDate = getLocalDateOrDefault(releaseDate),
    overview = overview.orEmpty(),
    trailerUrl = null,
    rating = null
)

fun SavedListLocalDto.toEntity() = SavedList(id, title, itemCount)

fun SavedList.toLocalDto() = SavedListLocalDto(id, title, itemCount)