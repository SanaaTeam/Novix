package com.sanaa.vod.history

import com.sanaa.vod.dataSource.local.history.dto.search.RecentViewedLocalDto
import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import com.sanaa.vod.history.dao.QueryDao
import com.sanaa.vod.history.dao.RecentViewedDao
import com.sanaa.vod.history.dao.WatchedMediaHistoryDao
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.search.search_param.MediaType

class LocalHistoryDataSourceImplTest {
    private lateinit var dataSource: LocalHistoryDataSourceImpl
    private var queryDao: QueryDao = mockk(relaxed = true)
    private var recentViewedDao: RecentViewedDao = mockk(relaxed = true)
    private var watchedMediaHistoryDao: WatchedMediaHistoryDao = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        dataSource = LocalHistoryDataSourceImpl(queryDao, recentViewedDao, watchedMediaHistoryDao)
    }

    @Test
    fun `insertQuery should call upsertQuery on queryDao`() = runTest {
        // Given
        val query = "test query"
        // When
        dataSource.insertQuery(query)
        // Then
        coVerify { queryDao.upsertQuery(query, any()) }
    }

    @Test
    fun `getQueries should call getQueries on queryDao`() = runTest {
        // Given
        val limit = 10
        // When
        dataSource.getQueries(limit)
        // Then
        coVerify { queryDao.getQueries(limit) }
    }

    @Test
    fun `deleteQueryById should call deleteQueryById on queryDao`() = runTest {
        // Given
        val id = 1
        // When
        dataSource.deleteQueryById(id)
        // Then
        coVerify { queryDao.deleteQueryById(id) }
    }

    @Test
    fun `insertRecentViewed should call insertRecentViewed on recentViewedDao`() = runTest {
        // Given
        val recentViewed = RecentViewedLocalDto(
            id = 1,
            imageUrl = "test imageUrl",
            isSaved = false,
            mediaType = "test mediaType"
        )
        // When
        dataSource.insertRecentViewed(recentViewed)
        // Then
        coVerify { recentViewedDao.insertRecentViewed(recentViewed) }
    }

    @Test
    fun `getAllRecentViewed should call getAllRecentViewed on recentViewedDao`() = runTest {
        // Given
        val limit = 10
        // When
        dataSource.getAllRecentViewed(limit)
        // Then
        coVerify { recentViewedDao.getAllRecentViewed(limit) }
    }

    @Test
    fun `deleteAllRecentViewed should call deleteAllRecentViewed on recentViewedDao`() = runTest {
        // When
        dataSource.deleteAllRecentViewed()
        // Then
        coVerify { recentViewedDao.deleteAllRecentViewed() }
    }

    @Test
    fun `deleteAllQueries should call deleteAllQueries on queryDao`() = runTest {
        // When
        dataSource.deleteAllQueries()
        // Then
        coVerify { queryDao.deleteAllQueries() }
    }


    @Test
    fun `upsertWatchedMedia should call upsert on watchedMediaHistoryDao`() = runTest {
        // Given
        val watchedMedia = WatchedMediaHistoryLocalDto(
            id = 1,
            posterImageUrl = "test imageUrl",
            mediaType = "test mediaType",
            username = "username",
            genres = "",
        )
        // When
        dataSource.insertWatchedMediaHistory(watchedMedia)
        // Then
        coVerify { watchedMediaHistoryDao.insertWatchedMediaHistory(watchedMedia) }
    }

    @Test
    fun `getWatchedMedia should call getWatchedMediaHistory on watchedMediaHistoryDao`() = runTest {
        val username = "testUser"
        val mediaType = MediaType.MOVIE
        val genre = null

        dataSource.getWatchedMediaHistory(username, mediaType, genre)

        coVerify { watchedMediaHistoryDao.getWatchedMediaHistory(username, mediaType.name, genre) }
    }

    // ========== PAGINATION TESTS ==========

    @Test
    fun `getAllQueries should Handle zero limit`() = runTest {
        // Given
        val limit = 0

        // When
        dataSource.getQueries(limit)

        // Then
        coVerify { queryDao.getQueries(limit) }
    }

    @Test
    fun `getAllQueries should handle large limit`() = runTest {
        // Given
        val limit = 1000

        // When
        dataSource.getQueries(limit)

        // Then
        coVerify { queryDao.getQueries(limit) }
    }

    @Test
    fun `getAllQueries should handle negative limit`() = runTest {
        // Given
        val limit = -10

        // When
        dataSource.getQueries(limit)

        // Then
        coVerify { queryDao.getQueries(limit) }
    }

    @Test
    fun `getAllRecentViewed should handle zero limit`() = runTest {
        // Given
        val limit = 0

        // When
        dataSource.getAllRecentViewed(limit)

        // Then
        coVerify { recentViewedDao.getAllRecentViewed(limit) }
    }

    @Test
    fun `getAllRecentViewed should handle large limit`() = runTest {
        // Given
        val limit = 500

        // When
        dataSource.getAllRecentViewed(limit)

        // Then
        coVerify { recentViewedDao.getAllRecentViewed(limit) }
    }

    @Test
    fun `getAllRecentViewed should handle negative limit`() = runTest {
        // Given
        val limit = -5

        // When
        dataSource.getAllRecentViewed(limit)

        // Then
        coVerify { recentViewedDao.getAllRecentViewed(limit) }
    }

    @Test
    fun `pagination should handle different limit values`() = runTest {
        // Given
        val limits = listOf(1, 5, 10, 20, 50, 100)

        // When & Then
        limits.forEach { limit ->
            dataSource.getQueries(limit)
            coVerify { queryDao.getQueries(limit) }
        }
    }

    @Test
    fun `pagination should handle consecutive calls`() = runTest {
        // Given
        val limit1 = 10
        val limit2 = 20

        // When
        dataSource.getQueries(limit1)
        dataSource.getQueries(limit2)

        // Then
        coVerify { queryDao.getQueries(limit1) }
        coVerify { queryDao.getQueries(limit2) }
    }

    @Test
    fun `pagination should handle recent viewed consecutive calls`() = runTest {
        // Given
        val limit1 = 5
        val limit2 = 15

        // When
        dataSource.getAllRecentViewed(limit1)
        dataSource.getAllRecentViewed(limit2)

        // Then
        coVerify { recentViewedDao.getAllRecentViewed(limit1) }
        coVerify { recentViewedDao.getAllRecentViewed(limit2) }
    }

    @Test
    fun `pagination should handle mixed operations`() = runTest {
        // Given
        val queryLimit = 25
        val recentLimit = 30

        // When
        dataSource.getQueries(queryLimit)
        dataSource.getAllRecentViewed(recentLimit)

        // Then
        coVerify { queryDao.getQueries(queryLimit) }
        coVerify { recentViewedDao.getAllRecentViewed(recentLimit) }
    }

    @Test
    fun `pagination should handle very large limits`() = runTest {
        // Given
        val veryLargeLimit = 10000

        // When
        dataSource.getQueries(veryLargeLimit)
        dataSource.getAllRecentViewed(veryLargeLimit)

        // Then
        coVerify { queryDao.getQueries(veryLargeLimit) }
        coVerify { recentViewedDao.getAllRecentViewed(veryLargeLimit) }
    }
}