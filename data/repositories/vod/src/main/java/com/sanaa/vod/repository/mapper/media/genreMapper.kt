package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.GenreDto
import entity.Genre


fun Genre.toDto(): GenreDto {
    return GenreDto(
        id = id,
    )
}


fun GenreDto.toEntity(): Genre {
    return Genre(
        id = id ?: 0,
        name = name.orEmpty()
    )
}
