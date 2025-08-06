package com.sanaa.vod.repository

import com.google.common.truth.Truth
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto
import com.sanaa.vod.dataSource.remote.dto.movie.MovieDto
import com.sanaa.vod.util.exceptions.ConnectionException
import entity.Movie
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
import repository.SavedMovieStatusProvider
import usecase.custom_list.custom_list_param.SavedList
import kotlin.test.Ignore

class SavedListRepositoryImplTest {

    private val prefs: PreferencesManager = mockk(relaxed = true)
    private val remoteLists: RemoteSavedListDataSource = mockk(relaxed = true)
    private val remoteMovies: RemoteMovieDataSource = mockk(relaxed = true)
    private val savedMovieStatusProvider: SavedMovieStatusProvider = mockk(relaxed = true)

    private lateinit var repository: SavedListRepositoryImpl

    private val dummyListsDto = listOf(
        SavedListDto(id = LIST_ID, title = "Watch-Later"),
        SavedListDto(id = LIST_ID + 1, title = "Favorites")
    )

    private val dummyItemsDto = listOf(
        SavedItemDto(id = 1, title = "A"),
        SavedItemDto(id = 2, title = "B")
    )

    private val dummyMovieDto1 = MovieDto(id = 1, title = "A")
    private val dummyMovieDto2 = MovieDto(id = 2, title = "B")

    @BeforeEach
    fun setup() {
        every { prefs.sessionId } returns MutableStateFlow(SESSION_ID)

        repository =
            SavedListRepositoryImpl(remoteLists, remoteMovies, prefs, savedMovieStatusProvider)
    }

    @Ignore
    @Test
    fun `getSavedLists returns user's lists`() = runTest {
        coEvery { remoteLists.fetchUserLists(SESSION_ID, PAGE) } returns dummyListsDto

        val lists: List<SavedList> = repository.getSavedLists()

        Truth.assertThat(lists).hasSize(2)
        Truth.assertThat(lists.first().title).isEqualTo("Watch-Later")
    }

    @Test
    fun `createSavedList returns created list`() = runTest {
        coEvery { remoteLists.createList(SESSION_ID, "Watch-Later", any()) } returns
                SavedListDto(id = LIST_ID, title = "Watch-Later")

        val created: SavedList = repository.createSavedList("Watch-Later")

        Truth.assertThat(created.id).isEqualTo(LIST_ID)
    }

    @Test
    fun `deleteSavedList completes without error`() = runTest {
        coJustRun { remoteLists.deleteList(SESSION_ID, LIST_ID) }

        repository.deleteSavedList(LIST_ID)

        coVerify(exactly = 1) { remoteLists.deleteList(SESSION_ID, LIST_ID) }
    }

    @Ignore
    @Test
    fun `getAllMoviesInList returns movies contained in the list`() = runTest {
        coEvery { remoteLists.fetchListItems(LIST_ID, null) } returns dummyItemsDto
        coEvery { remoteMovies.fetchMovieDetails(1) } returns dummyMovieDto1
        coEvery { remoteMovies.fetchMovieDetails(2) } returns dummyMovieDto2

        val movies: List<Movie> = repository.getAllMoviesInList(LIST_ID, PAGE)

        Truth.assertThat(movies).hasSize(2)
    }

    @Test
    fun `addMovieToList returns true when successful`() = runTest {
        coEvery { remoteLists.addItem(SESSION_ID, LIST_ID, MOVIE_ID) } returns true

        val success = repository.addMovieToList(LIST_ID, MOVIE_ID)

        Truth.assertThat(success).isTrue()
    }

    @Test
    fun `removeMovieFromList returns true when successful`() = runTest {
        coEvery { remoteLists.removeItem(SESSION_ID, LIST_ID, MOVIE_ID) } returns true

        val success = repository.removeMovieFromList(LIST_ID, MOVIE_ID)

        Truth.assertThat(success).isTrue()
    }

    @Test
    fun `getSavedLists throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remoteLists.fetchUserLists(any(), any()) } throws ConnectionException()

        assertThrows<NoNetworkException> { repository.getSavedLists() }
    }

    private companion object {
        const val SESSION_ID = "session-123"
        const val ACCOUNT_ID = 42L
        const val LIST_ID = 7
        const val MOVIE_ID = 99
        const val PAGE = 1
    }
}

data class ListItemDto(
    val id: Int,
    val mediaType: String = "movie"
)