package com.sanaa.search.search_history

import com.sanaa.search.dataSource.local.dto.RecentViewedLocalDto
import com.sanaa.search.search_history.dao.QueryDao
import com.sanaa.search.search_history.dao.RecentViewedDao
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalSearchHistoryDataSourceImplTest {
    private lateinit var dataSource: LocalSearchHistoryDataSourceImpl
    private var queryDao: QueryDao = mockk(relaxed = true)
    private var recentViewedDao: RecentViewedDao = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        dataSource = LocalSearchHistoryDataSourceImpl(queryDao, recentViewedDao)
    }

    @Test
    fun `insertQuery should call insertQuery on queryDao`() = runTest {
        // Given
        val query = "test query"
        // When
        dataSource.insertQuery(query)
        // Then
        coVerify { queryDao.insertQuery(match { it.query == query }) }
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

    // ========== PAGINATION TESTS ==========

    @Test
    fun `getAllQueries_shouldHandleZeroLimit`() = runTest {
        // Given
        val limit = 0
        
        // When
        dataSource.getQueries(limit)
        
        // Then
        coVerify { queryDao.getQueries(limit) }
    }

    @Test
    fun `getAllQueries_shouldHandleLargeLimit`() = runTest {
        // Given
        val limit = 1000
        
        // When
        dataSource.getQueries(limit)
        
        // Then
        coVerify { queryDao.getQueries(limit) }
    }

    @Test
    fun `getAllQueries_shouldHandleNegativeLimit`() = runTest {
        // Given
        val limit = -10
        
        // When
        dataSource.getQueries(limit)
        
        // Then
        coVerify { queryDao.getQueries(limit) }
    }

    @Test
    fun `getAllRecentViewed_shouldHandleZeroLimit`() = runTest {
        // Given
        val limit = 0
        
        // When
        dataSource.getAllRecentViewed(limit)
        
        // Then
        coVerify { recentViewedDao.getAllRecentViewed(limit) }
    }

    @Test
    fun getAllRecentViewed_shouldHandleLargeLimit() = runTest {
        // Given
        val limit = 500
        
        // When
        dataSource.getAllRecentViewed(limit)
        
        // Then
        coVerify { recentViewedDao.getAllRecentViewed(limit) }
    }

    @Test
    fun `getAllRecentViewed_shouldHandleNegativeLimit`() = runTest {
        // Given
        val limit = -5
        
        // When
        dataSource.getAllRecentViewed(limit)
        
        // Then
        coVerify { recentViewedDao.getAllRecentViewed(limit) }
    }

    @Test
    fun `pagination_shouldHandleDifferentLimitValues`() = runTest {
        // Given
        val limits = listOf(1, 5, 10, 20, 50, 100)
        
        // When & Then
        limits.forEach { limit ->
            dataSource.getQueries(limit)
            coVerify { queryDao.getQueries(limit) }
        }
    }

    @Test
    fun `pagination_shouldHandleConsecutiveCalls`() = runTest {
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
    fun `pagination_shouldHandleRecentViewedConsecutiveCalls`() = runTest {
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
    fun `pagination_shouldHandleMixedOperations`() = runTest {
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
    fun `pagination_shouldHandleVeryLargeLimits`() = runTest {
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