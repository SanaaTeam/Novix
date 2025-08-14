package com.sanaa.vod.repository.mapper.custom_list

import com.sanaa.vod.dataSource.local.cache.dto.SavedListLocalDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto
import com.sanaa.vod.repository.mapper.media.getFullImageUrl
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Movie
import usecase.custom_list.custom_list_param.SavedList
import kotlin.time.Duration.Companion.minutes

fun SavedListDto.toEntity() = SavedList(id, title, itemCount)

fun SavedItemDto.toEntity() = Movie(
    id = id,
    posterImageUrl = getFullImageUrl(posterPath),
    title = title ?: originalTitle.orEmpty(),
    genres = emptyList(),
    imdbRating = voteAverage ?: -1f,
    duration = (-1).minutes,
    releaseDate = getLocalDateOrDefault(releaseDate),
    overview = overview.orEmpty(),
    trailerUrl = "",
    rating = -1
)

fun SavedListLocalDto.toEntity() = SavedList(id, title, itemCount)

fun SavedList.toLocalDto() = SavedListLocalDto(id, title, itemCount)