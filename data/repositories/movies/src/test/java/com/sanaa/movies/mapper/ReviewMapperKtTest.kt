package com.sanaa.movies.mapper

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
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

        val result = dto.toDomain()

        assertThat(result.id).isEqualTo(10)
        assertThat(result.authorName).isEqualTo("John Doe")
        assertThat(result.userHandle).isEqualTo("johnd")
        assertThat(result.avatarUrl).contains("/avatar.jpg")
        assertThat(result.rating).isEqualTo(8.0f)
        assertThat(result.content).isEqualTo("Amazing movie!")
    }

}