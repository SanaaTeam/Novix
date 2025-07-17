package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import entity.Review
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ReviewMapperKtTest{
    @Test
    fun `ReviewDtoResults toDomain maps correctly`() {
        val dto = ReviewDto.Results(
            id = "10",
            content = "Amazing movie!",
            createdAt = "2023-01-01",
            authorDetails = ReviewDto.AuthorDetailsDto(
                name = "John Doe",
                username = "johnd",
                avatarPath = "/avatar.jpg",
                rating = 8.0f
            )
        )

        val expected = Review(
            id = 10,
            authorName = "John Doe",
            userHandle = "johnd",
            avatarUrl = "https://image.tmdb.org/t/p/w500/avatar.jpg",
            rating = 8.0f,
            content = "Amazing movie!",
            createdDate = LocalDate(2023, 1, 1)
        )

        val result = dto.toDomain()

        assertThat(result).isEqualTo(expected)
    }

}