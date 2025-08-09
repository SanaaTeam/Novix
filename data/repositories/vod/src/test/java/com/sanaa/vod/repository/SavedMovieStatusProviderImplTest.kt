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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SavedListsStatusProvider
import usecase.custom_list.custom_list_param.SavedList
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SavedMovieStatusProviderImplTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var remoteSavedListDataSource: RemoteSavedListDataSource
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var savedListsStatusProvider: SavedListsStatusProvider

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        remoteSavedListDataSource = mockk()
        preferencesManager = mockk()

        savedListsStatusProvider = SavedListsStatusProviderImpl(
            remoteSavedListDataSource,
            preferencesManager
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refreshItems populates savedIds only once`() = runTest {
        val sessionId = "session"
        val lists = listOf(
            SavedListDto(1, "List1"),
            SavedListDto(2, "List2")
        )
        val list1Items = listOf(SavedItemDto(11))
        val list2Items = listOf(SavedItemDto(22))

        coEvery { preferencesManager.sessionId } returns flowOf(sessionId)
        coEvery { remoteSavedListDataSource.fetchUserLists(sessionId) } returns lists
        coEvery { remoteSavedListDataSource.fetchListItems(1, null) } returns list1Items
        coEvery { remoteSavedListDataSource.fetchListItems(2, null) } returns list2Items

        savedListsStatusProvider.refreshItems()
        advanceUntilIdle()
        assertEquals(setOf(11, 22), savedListsStatusProvider.savedIds.value)

        savedListsStatusProvider.refreshItems()
        advanceUntilIdle()
        assertEquals(setOf(11, 22), savedListsStatusProvider.savedIds.value)
    }

    @Test
    fun `refreshLists populates savedLists with mapped SavedList`() = runTest {
        val sessionId = "session"
        val userLists = listOf(
            SavedListDto(10, "My List"),
            SavedListDto(20, "Other List")
        )
        coEvery { preferencesManager.sessionId } returns flowOf(sessionId)
        coEvery { remoteSavedListDataSource.fetchUserLists(sessionId) } returns userLists

        savedListsStatusProvider.refreshLists()
        advanceUntilIdle()

        val expected = userLists.map {
            SavedList(it.id, it.title, it.itemCount)
        }
        assertEquals(expected, savedListsStatusProvider.savedLists.value)
    }

    @Test
    fun `addList adds new list if not exists`() = runTest {
        val existing = SavedList(1, "Existing", 1)
        savedListsStatusProvider.addList(existing)
        assertEquals(listOf(existing), savedListsStatusProvider.savedLists.value)

        val newList = SavedList(2, "New List", 2)
        savedListsStatusProvider.addList(newList)
        assertEquals(listOf(existing, newList), savedListsStatusProvider.savedLists.value)
    }

    @Test
    fun `addList does not add duplicate list`() = runTest {
        val list = SavedList(1, "My List", 1)
        savedListsStatusProvider.addList(list)
        savedListsStatusProvider.addList(list)

        assertEquals(1, savedListsStatusProvider.savedLists.value.size)
    }

    @Test
    fun `isItemSaved triggers refreshItems if savedIds empty`() = runTest {
        val sessionId = "session"
        val userLists = listOf(SavedListDto(1, "List"))
        val items = listOf(SavedItemDto(123))

        coEvery { preferencesManager.sessionId } returns flowOf(sessionId)
        coEvery { remoteSavedListDataSource.fetchUserLists(sessionId) } returns userLists
        coEvery { remoteSavedListDataSource.fetchListItems(1, null) } returns items

        val result = savedListsStatusProvider.isItemSaved(123)
        advanceUntilIdle()

        assertTrue(result)
    }

    @Test
    fun `isItemSaved returns false if id not in savedIds`() = runTest {
        savedListsStatusProvider.markItemSaved(10)
        val result = savedListsStatusProvider.isItemSaved(99)
        advanceUntilIdle()
        assertFalse(result)
    }

    @Test
    fun `markItemSaved adds id to savedIds`() = runTest {
        savedListsStatusProvider.markItemSaved(5)
        assertTrue(savedListsStatusProvider.savedIds.value.contains(5))
    }

    @Test
    fun `markItemUnsaved removes id from savedIds`() = runTest {
        savedListsStatusProvider.markItemSaved(9)
        assertTrue(savedListsStatusProvider.savedIds.value.contains(9))

        savedListsStatusProvider.markItemUnsaved(9)
        assertFalse(savedListsStatusProvider.savedIds.value.contains(9))
    }
}
