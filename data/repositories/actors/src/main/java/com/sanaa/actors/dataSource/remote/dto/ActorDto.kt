package com.sanaa.actors.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDto(
    @SerialName("id") val id: Int,

    @SerialName("name") val name: String? = null,

    @SerialName("profile_path") val profileImagePath: String? = null,

    @SerialName("adult") val isAdult: Boolean? = null,

    @SerialName("also_known_as") val alsoKnownAs: List<String>? = null,

    @SerialName("biography") val biography: String? = null,

    @SerialName("birthday") val birthDay: String? = null,

    @SerialName("deathday") val deathDay: String? = null,

    @SerialName("gender") val gender: Int? = null,

    @SerialName("homepage") val homepage: String? = null,

    @SerialName("imdb_id") val imdbId: String? = null,

    @SerialName("known_for_department") val knownForDepartment: String? = null,

    @SerialName("place_of_birth") val placeOfBirth: String? = null,

    @SerialName("popularity") val popularity: Double? = null
)