package com.sanaa.vod.repository.mapper.media


import com.sanaa.vod.dataSource.remote.dto.review.AuthorDetailsDto
import com.sanaa.vod.dataSource.remote.dto.review.ReviewDto
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.assertNotNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ReviewMapperTest {

    @Test
    fun `toEntity maps full ReviewDto with TMDB image path`() {
        val dto = ReviewDto(
            id = "rev1",
            content = "Amazing show!",
            authorDetails = AuthorDetailsDto(
                name = "Haider",
                username = "haider_dev",
                avatarPath = "/avatar123.jpg",
                rating = 8.5f
            ),
            createdAt = "2024-04-25T12:00:00Z"
        )

        val result = dto.toEntity()

        assertEquals("rev1", result.id)
        assertEquals("Amazing show!", result.content)
        assertEquals("Haider", result.authorName)
        assertEquals("haider_dev", result.userHandle)
        assertEquals("https://image.tmdb.org/t/p/w185/avatar123.jpg", result.avatarUrl)
        assertEquals(8.5f, result.rating)
        assertEquals(LocalDate(2024, 4, 25), result.createdDate)
    }

    @Test
    fun `toEntity handles avatarPath with full URL starting with slash`() {
        val dto = ReviewDto(
            id = "rev2",
            content = "Nice plot.",
            authorDetails = AuthorDetailsDto(
                name = "Ali",
                username = "ali93",
                avatarPath = "/https://image.server.com/avatar.png",
                rating = 7.0f
            ),
            createdAt = "2023-12-10T08:30:00Z"
        )

        val result = dto.toEntity()

        assertEquals("https://image.server.com/avatar.png", result.avatarUrl)
    }

    @Test
    fun `toEntity handles avatarPath with null`() {
        val dto = ReviewDto(
            id = "rev4",
            content = "Meh.",
            authorDetails = AuthorDetailsDto(
                name = "Unknown",
                username = "anon",
                avatarPath = " ",
                rating = 5.0f
            ),
            createdAt = "2022-11-15T17:00:00Z"
        )

        val result = dto.toEntity()

        assertNotNull(result.avatarUrl)
    }

    @Test
    fun `toEntity handles avatarPath without slash or http`() {
        val dto = ReviewDto(
            id = "rev5",
            content = "Custom avatar path.",
            authorDetails = AuthorDetailsDto(
                name = "Nour",
                username = "nour_22",
                avatarPath = "custom/path/avatar.png",
                rating = 9.0f
            ),
            createdAt = "2024-08-05T09:15:00Z"
        )

        val result = dto.toEntity()

        assertEquals("custom/path/avatar.png", result.avatarUrl)
    }
}