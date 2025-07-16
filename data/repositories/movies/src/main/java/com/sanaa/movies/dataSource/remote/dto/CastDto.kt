package com.sanaa.movies.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class CastDto(

    @SerialName("id") var id: Int,
    @SerialName("cast") var cast: ArrayList<Cast> = arrayListOf(),
    @SerialName("crew") var crew: ArrayList<Crew> = arrayListOf()

) {
    @Serializable

    data class Cast(

        @SerialName("adult") var adult: Boolean? = null,
        @SerialName("gender") var gender: Int? = null,
        @SerialName("id") var id: Int,
        @SerialName("known_for_department") var knownForDepartment: String? = null,
        @SerialName("name") var name: String? = null,
        @SerialName("original_name") var originalName: String? = null,
        @SerialName("popularity") var popularity: Double? = null,
        @SerialName("profile_path") var profilePath: String? = null,
        @SerialName("cast_id") var castId: Int? = null,
        @SerialName("character") var character: String? = null,
        @SerialName("credit_id") var creditId: String? = null,
        @SerialName("order") var order: Int? = null

    )

    @Serializable

    data class Crew(

        @SerialName("adult") var adult: Boolean? = null,
        @SerialName("gender") var gender: Int? = null,
        @SerialName("id") var id: Int? = null,
        @SerialName("known_for_department") var knownForDepartment: String? = null,
        @SerialName("name") var name: String? = null,
        @SerialName("original_name") var originalName: String? = null,
        @SerialName("popularity") var popularity: Double? = null,
        @SerialName("profile_path") var profilePath: String? = null,
        @SerialName("credit_id") var creditId: String? = null,
        @SerialName("department") var department: String? = null,
        @SerialName("job") var job: String? = null

    )
}