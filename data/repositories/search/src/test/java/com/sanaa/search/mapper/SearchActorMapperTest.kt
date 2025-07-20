package com.sanaa.search.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.search.dataSource.local.dto.ActorLocalDto
import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import org.junit.jupiter.api.Test

class SearchActorMapperTest {

//ActorLocalDto
    @Test
    fun `should map correct id when ActorLocalDto has valid name`() {
        val dto = createActorLocalDto(id = 1)
        val result= dto.toSearchOutput()
        assertThat(result.id).isEqualTo(1)
    }

    @Test
    fun `should map correct name when ActorLocalDto has valid id`() {
        val dto = createActorLocalDto(name = "Tom Hanks")
        val result= dto.toSearchOutput()
        assertThat(result.name).isEqualTo("Tom Hanks")
    }
    @Test
    fun `should map correct profileImageUrl when ActorLocalDto has valid profileImageUrl`() {
        val dto = createActorLocalDto(imagePath = "/tom.jpg")
        val result= dto.toSearchOutput()
        assertThat(result.profileImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/tom.jpg")
    }

    //ActorSearchDto to LocalDto

    @Test
    fun `should map correct id when ActorSearchDto has valid name`() {
        val dto = createActorSearchDto(id = 2)
        val result = dto.toLocalDto(language = "en")
        assertThat(result.id).isEqualTo(1)
    }

    @Test
    fun `should map correct name when ActorSearchDto has valid id`() {
        val dto = createActorSearchDto(name = "Emma Watson")
        val result = dto.toLocalDto(language = "en")
        assertThat(result.name).isEqualTo("Emma Watson")
    }
    @Test
    fun `should map correct profileImageUrl when ActorSearchDto has valid profileImageUrl`() {
        val dto = createActorSearchDto(profileImagePath = "/emma.jpg")
        val result = dto.toLocalDto(language = "en")
        assertThat(result.imagePath).isEqualTo("https://image.tmdb.org/t/p/w500/emma.jpg")
    }
    @Test
    fun `should map correct language when converting ActorSearchDto to ActorLocalDto`() {
        val dto = createActorSearchDto(id = 2, name = "Emma Watson", profileImagePath = "/emma.jpg")
        val result = dto.toLocalDto(language = "en")
        assertThat(result.language).isEqualTo("en")
    }

//ActorSearchDto to SearchActorOutput
    @Test
    fun `should map correct id when ActorSearchDto has valid id`() {
        val dto = createActorSearchDto(id = 3)
        val result = dto.toSearchOutput()
        assertThat(result.id).isEqualTo(3)
    }

    @Test
    fun `should map correct name when ActorSearchDto has valid name`() {
        val dto = createActorSearchDto(name = "Chris Evans")
        val result = dto.toSearchOutput()
        assertThat(result.name).isEqualTo("Chris Evans")
    }

    @Test
    fun `should map profileImageUrl toSearchOutput correct  when ActorSearchDto has valid profileImageUrl`() {
        val dto = createActorSearchDto(profileImagePath = "/chris.jpg")
        val result = dto.toSearchOutput()
        assertThat(result.profileImageUrl).isEqualTo("https://image.tmdb.org/t/p/w500/chris.jpg")
    }

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