package com.sanaa.presentation.screen.state.mapper

import app.cash.turbine.test
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import search.usecase.ManageRecentViewedUseCase
import search.usecase.search_param.MediaType

class RecentViewedMapperTest {
    @Test
    fun `toUiState should map RecentViewedMedia to RecentViewedUiModel correctly`() {
        // Arrange
        val media = createRecentViewedMedia(1, "poster1", MediaTypeUi.MOVIE, true)

        // Act
        val result = media.toUiState()

        // Assert
        val expected = createExpectedUiModel(1, "poster1", MediaTypeUi.MOVIE, true)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `List toUiState should map list of RecentViewedMedia to list of RecentViewedUiModel`() {
        // Arrange
        val list = listOf(
            createRecentViewedMedia(1, "poster1", MediaTypeUi.MOVIE, true),
            createRecentViewedMedia(2, "poster2", MediaTypeUi.TV_SERIES, false)
        )

        // Act
        val result = list.toUiState()

        // Assert
        val expected = listOf(
            createExpectedUiModel(1, "poster1", MediaTypeUi.MOVIE, true),
            createExpectedUiModel(2, "poster2", MediaTypeUi.TV_SERIES, false)
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Flow toUiState should map flow of RecentViewedMedia to RecentViewedUiModel`() = runTest {
        // Arrange
        val flow = flowOf(
            listOf(
                createRecentViewedMedia(10, "posterX", MediaTypeUi.MOVIE, false),
                createRecentViewedMedia(20, "posterY", MediaTypeUi.TV_SERIES, true)
            )
        )

        val expected = listOf(
            createExpectedUiModel(10, "posterX", MediaTypeUi.MOVIE, false),
            createExpectedUiModel(20, "posterY", MediaTypeUi.TV_SERIES, true)
        )

        // Act & Assert
        flow.toUiState().test {
            val result = awaitItem()
            assertThat(result).isEqualTo(expected)
            awaitComplete()
        }
    }

    companion object {
        fun createRecentViewedMedia(
            id: Int,
            imageUrl: String,
            mediaType: MediaTypeUi,
            isSaved: Boolean
        ): ManageRecentViewedUseCase.RecentViewedMedia {
            return ManageRecentViewedUseCase.RecentViewedMedia(
                id = id,
                posterImageUrl = imageUrl,
                mediaType = MediaType.valueOf(mediaType.name),
                isSaved = isSaved
            )
        }

        fun createExpectedUiModel(
            id: Int,
            imageUrl: String,
            mediaType: MediaTypeUi,
            isSaved: Boolean
        ): RecentViewedUiModel {
            return RecentViewedUiModel(
                id = id,
                imageUrl = imageUrl,
                mediaType = mediaType,
                isSaved = isSaved
            )
        }
    }
}