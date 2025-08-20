package com.sanaa.vod.repository.mapper.savedList

import com.sanaa.vod.dataSource.local.cache.dto.SavedListLocalDto
import usecase.custom_list.custom_list_param.SavedList

fun SavedListLocalDto.toEntity():SavedList{
    return SavedList(
        id = id,
        title =listName,
        itemCount = movieIds.split(",")
            .filter { id -> id.isNotBlank() }.size,
        itemsIds = movieIds.split(",").filter { id -> id.isNotBlank() }
            .map { id -> id.toInt() }
    )
}

fun List<SavedListLocalDto>.toEntity():List<SavedList>{
    return map {savedListLocalDto ->
        savedListLocalDto.toEntity()
    }
}

fun SavedList.toLocalDto(): SavedListLocalDto {
    return SavedListLocalDto(
        id = id,
        listName = title,
        movieIds = itemsIds.joinToString(separator = ",") { it.toString() }
    )
}