package com.sanaa.vod.mapper.media

import com.sanaa.vod.dataSource.remote.dto.SeasonDto
import entity.Season

fun SeasonDto.toEntity(): Season {
    return Season(
        id = id,
        title = name,
        overview = overview,
        number = seasonNumber,
        episodes = episodes.map { it.toEntity() })
}