package com.sanaa.search.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.search.dataSource.local.LocalSearchHistoryDataSource
import com.sanaa.search.dataSource.local.dto.QueryLocalDto
import com.sanaa.search.dataSource.local.dto.RecentViewedLocalDto
import com.sanaa.search.mapper.toEntity
import exceptions.FailedToAddException
import exceptions.FailedToDeleteException
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
import usecase.search.MediaType

class SearchHistoryRepositoryImplTest {
    private lateinit var repository: SearchHistoryRepositoryImpl
    private var localDataSource: LocalSearchHistoryDataSource = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        repository = SearchHistoryRepositoryImpl(localDataSource)
    }

    @Test
    fun `getSearchHistory returns list of search history`() = runTest {
        // Given
        coEvery { localDataSource.getAllQueries(2) } returns flowOf(givenQueries)
        // When
        val expected = givenQueries.map { it.toEntity() }
        val result = repository.getSearchHistory(2)
        // Then
        assertThat(result.first()).isEqualTo(expected)
    }

    @Test
    fun `getSearchHistory throws exception when failed to retrieve search history`() = runTest {
        // Given
        coEvery { localDataSource.getAllQueries(2) } throws Exception()

        // When & Then
        assertThrows<RetrievingDataFailureException> { repository.getSearchHistory(2).first() }
    }

    @Test
    fun `addSearchHistory adds search history`() = runTest {
        // Given
        val query = "query"
        coEvery { localDataSource.insertQuery(query) } returns Unit
        // When
        repository.addSearchHistory(query)
        // Then
        coEvery { localDataSource.insertQuery(query) }
    }

    @Test
    fun `addSearchHistory throws exception when failed to add search history`() = runTest {
        // Given
        val query = "query"
        coEvery { localDataSource.insertQuery(query) } throws Exception()
        // When
        val result = runCatching { repository.addSearchHistory(query) }
        // Then
        assertThrows<FailedToAddException> { result.getOrThrow() }
    }

    @Test
    fun `clearSearchHistory clears search history`() = runTest {
        // Given
        coEvery { localDataSource.deleteAllQueries() } returns Unit
        // When
        repository.clearSearchHistory()
        // Then
        coEvery { localDataSource.deleteAllQueries() }
    }

    @Test
    fun `clearSearchHistory throws exception when failed to clear search history`() = runTest {
        // Given
        coEvery { localDataSource.deleteAllQueries() } throws Exception()
        // When
        val result = runCatching { repository.clearSearchHistory() }
        // Then
        assertThrows<FailedToDeleteException> { result.getOrThrow() }
    }

    @Test
    fun `removeSearchHistoryById removes search history by id`() = runTest {
        // Given
        val id = 1
        coEvery { localDataSource.deleteQueryById(id) } returns Unit
        // When
        repository.removeSearchHistoryById(id)
        // Then
        coEvery { localDataSource.deleteQueryById(id) }
    }

    @Test
    fun `removeSearchHistoryById throws exception when failed to remove search history`() =
        runTest {
            // Given
            val id = 1
            coEvery { localDataSource.deleteQueryById(id) } throws Exception()
            // When
            val result = runCatching { repository.removeSearchHistoryById(id) }
            // Then
            assertThrows<FailedToDeleteException> { result.getOrThrow() }
        }

    @Test
    fun `getRecentViewed returns list of recent viewed`() = runTest {
        // Given
        coEvery { localDataSource.getAllRecentViewed(2) } returns flowOf(givenRecentViewed)
        // When
        val expected = givenRecentViewed.map { it.toEntity() }
        val result = repository.getRecentViewed(2)
        // Then
        assertThat(result.first()).isEqualTo(expected)
    }

    @Test
    fun `getRecentViewed throws exception when failed to retrieve recent viewed`() = runTest {
        // Given
        coEvery { localDataSource.getAllRecentViewed(2) } throws Exception()
        // When & Then
        assertThrows<RetrievingDataFailureException> { repository.getRecentViewed(2).first() }
    }

    @Test
    fun `addRecentViewed adds recent viewed`() = runTest {
        // Given

        coEvery { localDataSource.insertRecentViewed(givenRecentViewed.first()) } returns Unit

        // When
        repository.addRecentViewedMedia(givenRecentViewed.first().toEntity())

        // Then
        coVerify {
            localDataSource.insertRecentViewed(match {
                it.id == 1 && it.imageUrl == "imageUrl1" && it.mediaType == MediaType.MOVIE.name && it.isSaved
            })
        }
    }

    @Test
    fun `addRecentViewed throws exception when failed to add recent viewed`() = runTest {
        // Given
        coEvery { localDataSource.insertRecentViewed(any()) } throws Exception()
        // When
        val result = runCatching {
            repository.addRecentViewedMedia(givenRecentViewed.first().toEntity())
        }
        assertThrows<FailedToAddException> { result.getOrThrow() }
    }


    @Test
    fun `addRecentViewedMedia throws exception when failed to add `() = runTest {
        // Given
        coEvery { localDataSource.insertRecentViewed(any()) } throws Exception()
        // When
        val result = runCatching {
            repository.addRecentViewedMedia(givenRecentViewed.first().toEntity())
        }
        // Then
        assertThrows<FailedToAddException> { result.getOrThrow() }

    }

    @Test
    fun `clearRecentViewed clears recent viewed`() = runTest {
        // Given
        coEvery { localDataSource.deleteAllRecentViewed() } returns Unit
        // When
        repository.clearRecentViewed()
        // Then
        coEvery { localDataSource.deleteAllRecentViewed() }
    }

    @Test
    fun `clearRecentViewed throws exception when failed to clear recent viewed`() = runTest {
        // Given
        coEvery { localDataSource.deleteAllRecentViewed() } throws Exception()
        // When
        val result = runCatching { repository.clearRecentViewed() }
        // Then
        assertThrows<FailedToDeleteException> { result.getOrThrow() }
    }


    val givenRecentViewed = listOf(
        RecentViewedLocalDto(
            id = 1,
            imageUrl = "imageUrl1",
            isSaved = true,
            mediaType = MediaType.MOVIE.name,
        ),
        RecentViewedLocalDto(
            id = 2,
            imageUrl = "imageUrl2",
            isSaved = false,
            mediaType = MediaType.MOVIE.name,
        )
    )

    val givenQueries = listOf(
        QueryLocalDto(id = 1, query = "query1", timestamp = 1L),
        QueryLocalDto(id = 2, query = "query2", timestamp = 2L)
    )
}

