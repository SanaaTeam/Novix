package com.sanaa.series.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.series.dto.AuthorDetailsDto
import com.sanaa.series.dto.ReviewDto
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ReviewMapperTest {


    @Test
    fun `should return correct author when ReviewDto is mapped`() {
        val result = createReviewDto(authorName = "Haider").toEntity()
        assertThat(result.authorName).isEqualTo("Haider")
    }


    @Test
    fun `should return correct id when ReviewDto is mapped`() {
        val result = createReviewDto(id = "123").toEntity()
        assertEquals("123", result.id)
    }

    @Test
    fun `should return correct username when ReviewDto is mapped`() {
        val result = createReviewDto(username = "haider123").toEntity()
        assertEquals("haider123", result.userHandle)
    }

    @Test
    fun `should return correct avatarPath when ReviewDto is mapped`() {
        val result = createReviewDto(avatarPath = "/avatar.png").toEntity()
        assertEquals("https://image.tmdb.org/t/p/w185/avatar.png", result.avatarUrl)
    }

    @Test
    fun `should return correct rating when ReviewDto is mapped`() {
        val result = createReviewDto(rating = 4.5f).toEntity()
        assertEquals(4.5f, result.rating)
    }

    @Test
    fun `should return correct createdAt when ReviewDto is mapped`() {
        val result = createReviewDto(createdAt = "2024-07-15T12:34:56.000Z").toEntity()
        assertEquals(LocalDate(2024, 7, 15), result.createdDate)
    }

    @Test
    fun `should handles null avatarPath when avatarPath is null`() {
        val result = createReviewDto(avatarPath = null).toEntity()
        assertNull(result.avatarUrl)
    }

    @Test
    fun `should handles null rating when rating is null`() {
        val result = createReviewDto(rating = null).toEntity()
        assertNull(result.rating)
    }
    @Test
    fun `should return correct URL when avatarPath starts with https`() {
        val url = buildAvatarUrl("/https://external.com/avatar.jpg")
        assertEquals("https://external.com/avatar.jpg", url)
    }

    @Test
    fun `should return correct URL when avatarPath starts with http`() {
        val url = buildAvatarUrl("/http://external.com/avatar.jpg")
        assertEquals("http://external.com/avatar.jpg", url)
    }

    @Test
    fun `should return original avatarPath when it doesn't start with slash`() {
        val url = buildAvatarUrl("plainAvatarPath.jpg")
        assertEquals("plainAvatarPath.jpg", url)
    }


    private fun createReviewDto(
        id: String = "123",
        content: String = "Default review content",
        authorName: String = "Default Author",
        username: String = "defaultUser",
        avatarPath: String? = "/default_avatar.png",
        rating: Float? = 5.0f,
        createdAt: String = "2024-07-15T12:34:56.000Z"
    ) = ReviewDto(
        id = id,
        content = content,
        authorDetails = AuthorDetailsDto(
            name = authorName,
            username = username,
            avatarPath = avatarPath,
            rating = rating
        ),
        createdAt = createdAt
    )
}