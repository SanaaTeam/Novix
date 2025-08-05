package com.sanaa.vod.mapper.custom_list

import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto
import entity.Movie
import kotlinx.datetime.LocalDate
import usecase.custom_list.custom_list_param.SavedList

/* ---------- list ---------- */
fun SavedListDto.toDomain() = SavedList(id, title)
fun SavedList.toDto() = SavedListDto(id = id, title = title)

/* ---------- item ---------- */
fun SavedItemDto.toDomain() = Movie(
    id = id,
    posterImageUrl = posterPath ?: "",
    title = title ?: "",
    genres = emptyList(),
    imdbRating = voteAverage,
    duration = null,
    releaseDate = LocalDate(2023, 5, 20),
    rating = null
)