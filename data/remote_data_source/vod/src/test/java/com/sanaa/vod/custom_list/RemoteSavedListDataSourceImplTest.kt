package com.sanaa.vod.custom_list

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.custom_list.request.AddOrRemoveItemBody
import com.sanaa.vod.custom_list.request.CreateListBody
import com.sanaa.vod.custom_list.response.CreateListResponseDto
import com.sanaa.vod.custom_list.response.ListApiResponse
import com.sanaa.vod.custom_list.response.TmdbStatusResponseDto
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemRemoteDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDetailsDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListRemoteDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RemoteSavedListDataSourceImplTest {

    private val apiService: SavedListApiService = mockk()
    private lateinit var dataSource: RemoteSavedListDataSource

    private val dummyLists = listOf(
        SavedListRemoteDto(id = LIST_ID, title = "Watch-Later"),
        SavedListRemoteDto(id = LIST_ID + 1, title = "Favorites")
    )
    private val dummyItems = listOf(
        SavedItemRemoteDto(id = 1, title = "A"),
        SavedItemRemoteDto(id = 2, title = "B")
    )
    private val dummyListDetails = SavedListDetailsDto(
        id = LIST_ID,
        name = "Watch-Later",
        description = "Movies to watch",
        itemCount = dummyItems.size,
        items = dummyItems
    )

    @BeforeEach
    fun setup() {
        dataSource = RemoteSavedListDataSourceImpl(apiService)
    }

    @Test
    fun `fetchUserLists() should return list of saved lists dto`() = runTest {
        coEvery { apiService.getUserLists(SESSION_ID, 1) } returns
                ListApiResponse(results = dummyLists)

        val lists = dataSource.fetchUserLists(SESSION_ID, 1)

        assertEquals(2, lists.size)
    }

    @Test
    fun `createList() should return created list dto`() = runTest {
        coEvery {
            apiService.createList(
                SESSION_ID,
                CreateListBody("Watch-Later", "Movies to watch", "en")
            )
        } returns CreateListResponseDto(true, 201, "Created", LIST_ID)

        coEvery { apiService.getListDetails(LIST_ID, null) } returns dummyListDetails

        val created = dataSource.createList(SESSION_ID, "Watch-Later", "Movies to watch")

        assertThat(created.id).isEqualTo(LIST_ID)
        assertThat(created.title).isEqualTo("Watch-Later")
    }

    @Test
    fun `deleteList() should complete without error`() = runTest {
        coEvery { apiService.deleteList(LIST_ID, SESSION_ID) } returns TmdbStatusResponseDto(
            success = true,
            statusCode = 300,
            statusMessage = "success"
        )

        dataSource.deleteList(SESSION_ID, LIST_ID)

        coVerify(exactly = 1) { apiService.deleteList(LIST_ID, SESSION_ID) }
    }

    @Test
    fun `fetchListItems() should return list of saved items dto`() = runTest {
        coEvery { apiService.getListDetails(LIST_ID, 1) } returns dummyListDetails

        val items = dataSource.fetchListItems(LIST_ID, 1)

        assertEquals(2, items.size)
    }

    @Test
    fun `addItem() should return true when movie successfully added`() = runTest {
        coEvery {
            apiService.addItem(
                LIST_ID,
                SESSION_ID,
                AddOrRemoveItemBody(MOVIE_ID)
            )
        } returns TmdbStatusResponseDto(success = true)

        val success = dataSource.addItem(SESSION_ID, LIST_ID, MOVIE_ID)

        assertThat(success).isTrue()
    }

    @Test
    fun `removeItem() should return true when movie successfully removed`() = runTest {
        coEvery {
            apiService.removeItem(
                LIST_ID,
                SESSION_ID,
                AddOrRemoveItemBody(MOVIE_ID)
            )
        } returns TmdbStatusResponseDto(success = true)

        val success = dataSource.removeItem(SESSION_ID, LIST_ID, MOVIE_ID)

        assertThat(success).isTrue()
    }

    private companion object {
        const val SESSION_ID = "session-123"
        const val LIST_ID = 7
        const val MOVIE_ID = 99
    }
}