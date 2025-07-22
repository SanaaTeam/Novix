package com.sanaa.vod.mapper.search

import com.google.common.truth.Truth
import com.sanaa.vod.dataSource.local.search.dto.ActorLocalDto
import com.sanaa.vod.dataSource.remote.search.dto.ActorSearchDto
import com.sanaa.vod.mapper.actor.getFullImageUrl
import org.junit.jupiter.api.Test

class SearchActorMapperTest {

    //ActorLocalDto
    @Test
    fun `should map correct id when ActorLocalDto has valid name`() {
        val dto = createActorLocalDto(id = 1)
        val result = dto.toSearchOutput()
        Truth.assertThat(result.id).isEqualTo(1)
    }

    @Test
    fun `should map correct name when ActorLocalDto has valid id`() {
        val dto = createActorLocalDto(name = "Tom Hanks")
        val result = dto.toSearchOutput()
        Truth.assertThat(result.name).isEqualTo("Tom Hanks")
    }

    @Test
    fun `should map correct profileImageUrl when ActorLocalDto has valid profileImageUrl`() {
        val dto = createActorLocalDto(imagePath = "/tom.jpg")
        val result = dto.toSearchOutput()
        Truth.assertThat(result.profileImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/tom.jpg")
    }

    //ActorSearchDto to LocalDto

    @Test
    fun `should map correct id when ActorSearchDto has valid name`() {
        val dto = createActorSearchDto(id = 2)
        val result = dto.toLocalDto(language = "en")
        Truth.assertThat(result.id).isEqualTo(2)
    }

    @Test
    fun `should map correct name when ActorSearchDto has valid id`() {
        val dto = createActorSearchDto(name = "Emma Watson")
        val result = dto.toLocalDto(language = "en")
        Truth.assertThat(result.name).isEqualTo("Emma Watson")
    }

    @Test
    fun `should map correct profileImageUrl when ActorSearchDto has valid profileImageUrl`() {
        val dto = createActorSearchDto(profileImagePath = "/emma.jpg")
        val result = dto.toLocalDto(language = "en")
        Truth.assertThat(result.imagePath).isEqualTo("https://image.tmdb.org/t/p/w500/emma.jpg")
    }

    @Test
    fun `should map correct language when converting ActorSearchDto to ActorLocalDto`() {
        val dto = createActorSearchDto(id = 2, name = "Emma Watson", profileImagePath = "/emma.jpg")
        val result = dto.toLocalDto(language = "en")
        Truth.assertThat(result.language).isEqualTo("en")
    }

    //ActorSearchDto to SearchActorOutput
    @Test
    fun `should map correct id when ActorSearchDto has valid id`() {
        val dto = createActorSearchDto(id = 3)
        val result = dto.toSearchOutput()
        Truth.assertThat(result.id).isEqualTo(3)
    }

    @Test
    fun `should map correct name when ActorSearchDto has valid name`() {
        val dto = createActorSearchDto(name = "Chris Evans")
        val result = dto.toSearchOutput()
        Truth.assertThat(result.name).isEqualTo("Chris Evans")
    }

    @Test
    fun `should map profileImageUrl toSearchOutput correct  when ActorSearchDto has valid profileImageUrl`() {
        val dto = createActorSearchDto(profileImagePath = "/chris.jpg")
        val result = dto.toSearchOutput()
        Truth.assertThat(result.profileImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/chris.jpg")
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
    private fun createActorLocalDto(
        id: Int = 1,
        name: String = "Default",
        imagePath: String? = null,
        language: String = "en"
    ) = ActorLocalDto(
        id = id,
        name = name,
        imagePath = imagePath,
        language = language
    )

    private fun createActorSearchDto(
        id: Int = 1,
        name: String? = null,
        profileImagePath: String? = null
    ) = ActorSearchDto(
        id = id,
        name = name,
        profileImagePath = profileImagePath
    )
}