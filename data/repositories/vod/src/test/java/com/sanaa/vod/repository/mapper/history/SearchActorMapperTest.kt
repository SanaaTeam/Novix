package com.sanaa.vod.repository.mapper.history

import com.google.common.truth.Truth
import com.sanaa.vod.dataSource.remote.dto.search.ActorSearchDto
import com.sanaa.vod.repository.mapper.media.getFullImageUrl
import org.junit.jupiter.api.Test

class SearchActorMapperTest {

    //ActorSearchDto to SearchActorOutput
    @Test
    fun `should map correct id when ActorSearchDto has valid id`() {
        val dto = createActorSearchDto(id = 3)
        val result = dto.toEntity()
        Truth.assertThat(result.id).isEqualTo(3)
    }

    @Test
    fun `should map correct name when ActorSearchDto has valid name`() {
        val dto = createActorSearchDto(name = "Chris Evans")
        val result = dto.toEntity()
        Truth.assertThat(result.name).isEqualTo("Chris Evans")
    }

    @Test
    fun `should map profileImageUrl toSearchOutput correct  when ActorSearchDto has valid profileImageUrl`() {
        val dto = createActorSearchDto(profileImagePath = "/chris.jpg")
        val result = dto.toEntity()
        Truth.assertThat(result.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/chris.jpg")
    }

    @Test
    fun `should return empty imageUrl when path is null`() {
        val result = getFullImageUrl(null)
        Truth.assertThat(result).isEqualTo("")
    }

    @Test
    fun `should return empty imageUrl when path is blank`() {
        val result = getFullImageUrl(" ")
        Truth.assertThat(result).isEqualTo("")
    }

    @Test
    fun `should return full imageUrl when path is valid`() {
        val result = getFullImageUrl("/somePath.jpg")
        Truth.assertThat(result).isEqualTo("https://image.tmdb.org/t/p/w500/somePath.jpg")
    }

    // Helper functions

    private fun createActorSearchDto(
        id: Int = 1,
        name: String? = null,
        profileImagePath: String? = null
    ) = ActorSearchDto(
        id = id,
        name = name,
        profileImagePath = profileImagePath,
        gender = null,
        knownForDepartment = null
    )
}