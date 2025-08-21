package com.sanaa.vod.repository.mapper.savedList

import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemRemoteDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListRemoteDto
import com.sanaa.vod.repository.mapper.media.getFullImageUrl
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Movie
import usecase.custom_list.custom_list_param.SavedList
import kotlin.time.Duration.Companion.minutes

fun SavedListRemoteDto.toEntity(itemsIds: List<Int>) = SavedList(id, title, itemCount,itemsIds)
fun List<SavedListRemoteDto>.toEntity(itemsIds:List<Int>) = map { it.toEntity(itemsIds) }

fun SavedItemRemoteDto.toEntity() = Movie(
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