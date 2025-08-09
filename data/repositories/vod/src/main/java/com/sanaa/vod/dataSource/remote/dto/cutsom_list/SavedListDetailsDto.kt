package com.sanaa.vod.dataSource.remote.dto.cutsom_list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedListDetailsDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("created_by") val createdBy: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("favorite_count") val favoriteCount: Int = 0,
    @SerialName("item_count") val itemCount: Int = 0,
    @SerialName("iso_639_1") val language: String = "en",
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("page") val page: Int = 1,
    @SerialName("total_pages") val totalPages: Int = 1,
    @SerialName("total_results") val totalResults: Int = itemCount,
    @SerialName("items") val items: List<SavedItemDto> = emptyList()
) {
    fun toListDto() = SavedListDto(
        id = id,
        title = name,
        description = description,
        favoriteCount = favoriteCount,
        itemCount = itemCount,
        language = language,
        listType = "movie",
        posterPath = posterPath
    )
}