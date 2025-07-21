package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.AuthorDetailsDto
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ReviewMapperKtTest {
    @Test
    fun `should map content correctly when ReviewDto is valid`() {
        val dto = validReviewDto()
        val result = dto.toEntity()
        assertThat(result.content).isEqualTo("Amazing movie!")
    }

    @Test
    fun `should map authorName correctly when ReviewDto is valid`() {
        val dto = validReviewDto()
        val result = dto.toEntity()
        assertThat(result.authorName).isEqualTo("John Doe")
    }

    @Test
    fun `should map userHandle correctly when ReviewDto is valid`() {
        val dto = validReviewDto()
        val result = dto.toEntity()
        assertThat(result.userHandle).isEqualTo("johnd")
    }

    @Test
    fun `should map rating correctly when ReviewDto is valid`() {
        val dto = validReviewDto()
        val result = dto.toEntity()
        assertThat(result.rating).isEqualTo(8.0f)
    }

    @Test
    fun `should parse createdDate correctly when ReviewDto has valid date`() {
        val dto = validReviewDto()
        val result = dto.toEntity()
        assertThat(result.createdDate).isEqualTo(LocalDate(2023, 1, 1))
    }

    @Test
    fun `should parse avatarUrl correctly when path starts with slash`() {
        val url = buildAvatarUrl("/avatar.jpg")
        assertThat(url).isEqualTo("https://image.tmdb.org/t/p/w185/avatar.jpg")
    }

    @Test
    fun `should return external URL when avatarPath starts with http`() {
        val url = buildAvatarUrl("/https://external.com/avatar.png")
        assertThat(url).isEqualTo("https://external.com/avatar.png")
    }

    @Test
    fun `should return original avatarPath when path doesn't start with slash`() {
        val url = buildAvatarUrl("custom_avatar.png")
        assertThat(url).isEqualTo("custom_avatar.png")
    }

    @Test
    fun `should return null when avatarPath is null`() {
        val url = buildAvatarUrl(null)
        assertThat(url).isNull()
    }

    @Test
    fun `should return null when avatarPath is blank`() {
        val url = buildAvatarUrl("")
        assertThat(url).isNull()
    }

    @Test
    fun `should parse id correctly when ReviewDto id is numeric`() {
        val dto = validReviewDto().copy(id = "42")
        val result = dto.toEntity()
        assertThat(result.id).isEqualTo("42")
    }

    private fun validReviewDto() = ReviewDto(
        id = "10",
        content = "Amazing movie!",
        createdAt = "2023-01-01T10:00:00Z",
        authorDetails = AuthorDetailsDto(
            name = "John Doe",
            username = "johnd",
            avatarPath = "/avatar.jpg",
            rating = 8.0f
        )
    )
}

