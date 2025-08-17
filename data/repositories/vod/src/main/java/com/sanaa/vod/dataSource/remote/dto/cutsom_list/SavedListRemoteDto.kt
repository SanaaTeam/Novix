package com.sanaa.vod.dataSource.remote.dto.cutsom_list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedListRemoteDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("favorite_count") val favoriteCount: Int = 0,
    @SerialName("item_count") val itemCount: Int = 0,
    @SerialName("iso_639_1") val language: String = "en",
    @SerialName("list_type") val listType: String? = null,
    @SerialName("poster_path") val posterPath: String? = null
)
