package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import repository.SavedListsStatusProvider

@OptIn(ExperimentalCoroutinesApi::class)
class SavedMovieStatusProviderImplTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var remoteSavedListDataSource: RemoteSavedListDataSource
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var savedListsStatusProvider: SavedListsStatusProvider


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        remoteSavedListDataSource = mockk()
        preferencesManager = mockk()

        savedListsStatusProvider = SavedListsStatusProviderImpl(
            remoteSavedListDataSource,
            preferencesManager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refresh should fetch all saved movie ids and populate the state`() = runTest {
        val sessionId = "test-session"
        val userLists = listOf(
            SavedListDto(id = 1, title = "Favorites", itemCount = 2),
            SavedListDto(id = 2, title = "Watch Later", itemCount = 2)
        )
        val list1Items = listOf(SavedItemDto(id = 101), SavedItemDto(id = 102))
        val list2Items = listOf(SavedItemDto(id = 201), SavedItemDto(id = 202))

        coEvery { preferencesManager.sessionId } returns flowOf(sessionId)
        coEvery { remoteSavedListDataSource.fetchUserLists(sessionId) } returns userLists
        coEvery { remoteSavedListDataSource.fetchListItems(1, null) } returns list1Items
        coEvery { remoteSavedListDataSource.fetchListItems(2, null) } returns list2Items

        savedListsStatusProvider.refreshItems()
        advanceUntilIdle()

        val expectedIds = setOf(101, 102, 201, 202)
        assertEquals(expectedIds, savedListsStatusProvider.savedIds.value)
    }

    @Test
    fun `isSaved should return true if movie is in saved list`() = runTest {
        val sessionId = "test-session"
        val userLists = listOf(SavedListDto(id = 1, title = "Favorites", itemCount = 1))
        val items = listOf(SavedItemDto(id = 999))

        coEvery { preferencesManager.sessionId } returns flowOf(sessionId)
        coEvery { remoteSavedListDataSource.fetchUserLists(sessionId) } returns userLists
        coEvery { remoteSavedListDataSource.fetchListItems(1, null) } returns items

        val result = savedListsStatusProvider.isItemSaved(999)
        advanceUntilIdle()

        assertTrue(result)
    }

    @Test
    fun `isSaved should return false if movie is not in saved list`() = runTest {
        val sessionId = "test-session"
        val userLists = listOf(SavedListDto(id = 1, title = "Favorites", itemCount = 1))
        val items = listOf(SavedItemDto(id = 999))

        coEvery { preferencesManager.sessionId } returns flowOf(sessionId)
        coEvery { remoteSavedListDataSource.fetchUserLists(sessionId) } returns userLists
        coEvery { remoteSavedListDataSource.fetchListItems(1, null) } returns items

        val result = savedListsStatusProvider.isItemSaved(123)
        advanceUntilIdle()

        assertFalse(result)
    }

    @Test
    fun `markSaved should add id to savedIds`() = runTest {
        savedListsStatusProvider.markItemSaved(55)
        assertTrue(savedListsStatusProvider.savedIds.value.contains(55))
    }

    @Test
    fun `markUnsaved should remove id from savedIds`() = runTest {
        savedListsStatusProvider.markItemSaved(99)
        assertTrue(savedListsStatusProvider.savedIds.value.contains(99))

        savedListsStatusProvider.markItemUnsaved(99)
        assertFalse(savedListsStatusProvider.savedIds.value.contains(99))
    }
}
