package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemRemoteDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListRemoteDto
import com.sanaa.vod.dataSource.remote.dto.movie.MovieDto
import com.sanaa.vod.util.exceptions.ConnectionException
import exceptions.NoNetworkException
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.SavedListsStatusProvider
import usecase.custom_list.custom_list_param.SavedList

class SavedListRepositoryImplTest {

    private val prefs: PreferencesManager = mockk(relaxed = true)
    private val remoteLists: RemoteSavedListDataSource = mockk(relaxed = true)
    private val remoteMovies: RemoteMovieDataSource = mockk(relaxed = true)
    private val savedListsStatusProvider: SavedListsStatusProvider = mockk(relaxed = true)

    private lateinit var repository: SavedListRepositoryImpl

    private val dummyListsDto = listOf(
        SavedListRemoteDto(id = LIST_ID, title = "Watch-Later"),
        SavedListRemoteDto(id = LIST_ID + 1, title = "Favorites")
    )

    private val dummyItemsDto = listOf(
        SavedItemRemoteDto(id = 1, title = "A"),
        SavedItemRemoteDto(id = 2, title = "B")
    )

    private val dummyMovieDto1 = MovieDto(id = 1, title = "A")
    private val dummyMovieDto2 = MovieDto(id = 2, title = "B")

    @BeforeEach
    fun setup() {
        every { prefs.sessionId } returns MutableStateFlow(SESSION_ID)

        repository =
            SavedListRepositoryImpl(remoteLists, remoteMovies, prefs, savedListsStatusProvider)
    }


    @Test
    fun `createSavedList returns created list`() = runTest {
        coEvery { remoteLists.createList(SESSION_ID, "Watch-Later", any()) } returns
                SavedListRemoteDto(id = LIST_ID, title = "Watch-Later")

        val created: SavedList = repository.createSavedList("Watch-Later")
        assertThat(created.id).isEqualTo(LIST_ID)
    }

    @Test
    fun `getSavedLists returns mapped saved lists on success`() = runTest {
        coEvery { remoteLists.fetchUserLists(SESSION_ID) } returns dummyListsDto

        val lists = repository.getLocalSavedLists()

        assertThat(lists).hasSize(2)
        assertThat(lists[0].id).isEqualTo(LIST_ID)
        assertThat(lists[1].id).isEqualTo(LIST_ID + 1)
    }

    @Test
    fun `getAllMoviesInList returns mapped movies with isSaved true`() = runTest {
        coEvery { remoteLists.fetchListItems(LIST_ID, PAGE) } returns dummyItemsDto
        coEvery { remoteMovies.fetchMovieDetails(1) } returns dummyMovieDto1
        coEvery { remoteMovies.fetchMovieDetails(2) } returns dummyMovieDto2
        coEvery { savedListsStatusProvider.isItemSaved(1) } returns true
        coEvery { savedListsStatusProvider.isItemSaved(2) } returns false

        val movies = repository.getMoviesInList(LIST_ID, PAGE)

        assertThat(movies).hasSize(2)
        assertThat(movies[0].id).isEqualTo(1)
    }

    @Test
    fun `deleteSavedList completes without error`() = runTest {
        coJustRun { remoteLists.deleteList(SESSION_ID, LIST_ID) }

        repository.deleteSavedList(LIST_ID)

        coVerify(exactly = 1) { remoteLists.deleteList(SESSION_ID, LIST_ID) }
    }


    @Test
    fun `addMovieToList returns true when successful`() = runTest {
        coEvery { remoteLists.addItem(SESSION_ID, LIST_ID, MOVIE_ID) } returns true

        val success = repository.addMovieToList(LIST_ID, MOVIE_ID)

        assertThat(success).isTrue()
    }

    @Test
    fun `removeMovieFromList returns true when successful`() = runTest {
        coEvery { remoteLists.removeItem(SESSION_ID, LIST_ID, MOVIE_ID) } returns true

        val success = repository.removeMovieFromList(LIST_ID, MOVIE_ID)

        assertThat(success).isTrue()
    }

    @Test
    fun `getSavedLists throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remoteLists.fetchUserLists(any(), any()) } throws ConnectionException()

        assertThrows<NoNetworkException> { repository.getLocalSavedLists() }
    }

    private companion object {
        const val SESSION_ID = "session-123"
        const val LIST_ID = 7
        const val MOVIE_ID = 99
        const val PAGE = 1
    }
}