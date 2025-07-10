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
    private lateinit var queryDao: QueryDao
    private lateinit var recentViewedDao: RecentViewedDao

    @BeforeEach
    fun setUp() {
        queryDao = mockk(relaxed = true)
        recentViewedDao = mockk(relaxed = true)
        dataSource = LocalSearchHistoryDataSourceImpl(queryDao, recentViewedDao)
    }

    @Test
    fun `insertQuery should call insertQuery on queryDao`() = runTest {
        // Given
        val query = "test query"
        // When
        dataSource.insertQuery(query)
        // Then
        coVerify { queryDao.insertQuery(any()) }
    }

    @Test
    fun `getAllQueries should call getAllQueries on queryDao`() = runTest {
        // Given
        val limit = 10
        // When
        dataSource.getAllQueries(limit)
        // Then
        coVerify { queryDao.getAllQueries(limit) }
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
            title = "test title",
            imageUrl = "test imageUrl",
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
}