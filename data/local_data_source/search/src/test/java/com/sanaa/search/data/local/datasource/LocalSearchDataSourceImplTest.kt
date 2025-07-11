package com.sanaa.search.data.local.datasource

import com.sanaa.search.data.local.dao.SearchDao
import com.sanaa.search.data.local.dao.SearchResultDao
import com.sanaa.search.data.local.entity.SearchLocalDto
import com.sanaa.search.data.local.entity.SearchResultLocalDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LocalSearchDataSourceImplTest {
    private lateinit var dataSource: LocalSearchDataSourceImpl
    private var searchDao: SearchDao = mockk(relaxed = true)
    private var searchResultDao: SearchResultDao = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        dataSource = LocalSearchDataSourceImpl(searchDao, searchResultDao)
    }

    @Test
    fun `saveSearchResult should call insertSearch and insert on DAOs`() = runTest {
        // Given
        val query = "test query"
        val language = "en"
        val itemId = 123
        val itemType = "movie"
        val searchId = 1L
        coEvery { searchDao.insertSearch(any()) } returns searchId

        // When
        dataSource.saveSearchResult(query, language, itemId, itemType)

        // Then
        coVerify { searchDao.insertSearch(match { it.query == query && it.language == language }) }
        coVerify { searchResultDao.insert(match { it.searchId == searchId && it.itemId == itemId && it.itemType == itemType }) }
    }

    @Test
    fun `getCachedResults should call getByQueryAndLanguage on searchResultDao and return result`() = runTest {
        // Given
        val query = "test query"
        val language = "en"
        val expected = listOf(SearchResultLocalDto(1L, 123, "movie"))
        coEvery { searchResultDao.getByQueryAndLanguage(query, language) } returns expected

        // When
        val result = dataSource.getCachedResults(query, language)

        // Then
        coVerify { searchResultDao.getByQueryAndLanguage(query, language) }
        assertEquals(expected, result)
    }

    @Test
    fun `clearExpiredResults should call deleteOldResults on searchResultDao`() = runTest {
        // When
        dataSource.clearExpiredResults()
        // Then
        coVerify { searchResultDao.deleteOldResults(any()) }
    }
} 