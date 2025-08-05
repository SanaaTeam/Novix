package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.local.history.LocalHistoryDataSource
import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import com.sanaa.vod.repository.mapper.history.toEntity
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import usecase.search.search_param.MediaType

class WatchingHistoryRepositoryImplTest {
    private lateinit var repository: HistoryRepositoryImpl
    private var localDataSource: LocalHistoryDataSource = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        repository = HistoryRepositoryImpl(localDataSource)
    }

    @Test
    fun `getWatchingHistory returns list of watching history`() = runTest {
        // Given
        val username = "testuser"
        val mediaType = MediaType.MOVIE
        coEvery { localDataSource.getWatchedMediaHistory(username, mediaType, null) } returns flowOf(givenWatchingHistoryDtos)

        // When
        val result = repository.getWatchingHistory(username, mediaType).first()

        // Then
        val expected = givenWatchingHistoryDtos.map { it.toEntity() }
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getWatchingHistory returns empty list when no history available`() = runTest {
        // Given
        val username = "testuser"
        coEvery { localDataSource.getWatchedMediaHistory(username, null, null) } returns flowOf(emptyList())

        // When
        val result = repository.getWatchingHistory(username, null).first()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getWatchingHistory throws exception when failed to retrieve watching history`() = runTest {
        // Given
        val username = "testuser"
        coEvery { localDataSource.getWatchedMediaHistory(username, null, null) } throws Exception()

        // When, Then
        assertThrows<RetrievingDataFailureException> { 
            repository.getWatchingHistory(username, null).first() 
        }
    }

    @Test
    fun `updateLastWatchedTime updates last watched time`() = runTest {
        // Given
        val username = "testuser"
        val mediaId = 1
        val mediaType = MediaType.MOVIE
        coEvery { localDataSource.updateLastWatchedTime(username, mediaId, mediaType) } returns Unit

        // When
        repository.updateLastWatchedTime(username, mediaId, mediaType)

        // Then
        coVerify { localDataSource.updateLastWatchedTime(username, mediaId, mediaType) }
    }


    @Test
    fun `getWatchingHistory filters by movie type correctly`() = runTest {
        // Given
        val username = "testuser"
        val mediaType = MediaType.MOVIE
        val movieHistoryDtos = listOf(
            dummyWatchingHistoryDto.copy(id = 1, mediaType = MediaType.MOVIE.name),
            dummyWatchingHistoryDto.copy(id = 2, mediaType = MediaType.MOVIE.name)
        )
        coEvery { localDataSource.getWatchedMediaHistory(username, mediaType, null) } returns flowOf(movieHistoryDtos)

        // When
        val result = repository.getWatchingHistory(username, mediaType).first()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.all { it.mediaType == MediaType.MOVIE }).isTrue()
    }

    @Test
    fun `getWatchingHistory filters by TV series type correctly`() = runTest {
        // Given
        val username = "testuser"
        val mediaType = MediaType.TV_SERIES
        val tvHistoryDtos = listOf(
            dummyWatchingHistoryDto.copy(id = 3, mediaType = MediaType.TV_SERIES.name),
            dummyWatchingHistoryDto.copy(id = 4, mediaType = MediaType.TV_SERIES.name)
        )
        coEvery { localDataSource.getWatchedMediaHistory(username, mediaType, null) } returns flowOf(tvHistoryDtos)

        // When
        val result = repository.getWatchingHistory(username, mediaType).first()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.all { it.mediaType == MediaType.TV_SERIES }).isTrue()
    }

    @Test
    fun `getWatchingHistory returns all types when mediaType is null`() = runTest {
        // Given
        val username = "testuser"
        val allHistoryDtos = listOf(
            dummyWatchingHistoryDto.copy(id = 1, mediaType = MediaType.MOVIE.name),
            dummyWatchingHistoryDto.copy(id = 2, mediaType = MediaType.TV_SERIES.name),
            dummyWatchingHistoryDto.copy(id = 3, mediaType = MediaType.MOVIE.name)
        )
        coEvery { localDataSource.getWatchedMediaHistory(username, null, null) } returns flowOf(allHistoryDtos)

        // When
        val result = repository.getWatchingHistory(username, null).first()

        // Then
        assertThat(result).hasSize(3)
        assertThat(result.any { it.mediaType == MediaType.MOVIE }).isTrue()
        assertThat(result.any { it.mediaType == MediaType.TV_SERIES }).isTrue()
    }


    companion object {
        private val dummyWatchingHistoryDto = WatchedMediaHistoryLocalDto(
            id = 1,
            username = "testuser",
            posterImageUrl = "test_url",
            mediaType = MediaType.MOVIE.name,
            genres = "28,12",
            isSaved = false,
            timestamp = System.currentTimeMillis()
        )
        private val givenWatchingHistoryDtos = listOf(
            dummyWatchingHistoryDto,
            dummyWatchingHistoryDto.copy(id = 2),
            dummyWatchingHistoryDto.copy(id = 3)
        )
    }
} 