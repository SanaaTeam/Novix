package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemRemoteDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListRemoteDto
import com.sanaa.vod.dataSource.remote.dto.movie.MovieDto
import com.sanaa.vod.repository.mapper.savedList.toEntity
import com.sanaa.vod.repository.mapper.savedList.toLocalDto
import entity.Movie
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.custom_list.custom_list_param.SavedList

class SavedListRepositoryImplTest {

    private val prefs: PreferencesManager = mockk(relaxed = true)
    private val remoteDataSource: RemoteSavedListDataSource = mockk(relaxed = true)
    private val localDataSource: LocalSavedMovieDataSource = mockk(relaxed = true)

    private lateinit var repository: SavedListRepositoryImpl

    private val dummyRemoteLists = listOf(
        SavedListRemoteDto(id = LIST_ID, title = "Watch-Later"),
        SavedListRemoteDto(id = LIST_ID + 1, title = "Favorites")
    )

    private val dummyItemsDto = listOf(
        SavedItemRemoteDto(id = 1, title = "A"),
        SavedItemRemoteDto(id = 2, title = "B")
    )

    private val dummyMoviesDto = listOf(
        MovieDto(id = 1, title = "A"),
        MovieDto(id = 2, title = "B")
    )

    @BeforeEach
    fun setup() {
        every { prefs.sessionId } returns MutableStateFlow(SESSION_ID)
        repository = SavedListRepositoryImpl(remoteDataSource, localDataSource, prefs)
    }

    @Test
    fun `getLocalSavedLists emits mapped data from local`() = runTest {
        // Arrange
        val localDtos = dummyRemoteLists.map { it.toEntity(emptyList()).toLocalDto() }
        coEvery { localDataSource.getAllLists() } returns flowOf(localDtos)

        // Act
        val result = repository.getLocalSavedLists()
        val collected = result.first()

        // Assert
        assertThat(collected).hasSize(2)
        assertThat(collected[0].id).isEqualTo(LIST_ID)
    }

    @Test
    fun `refreshSavedLists clears and inserts fetched lists`() = runTest {
        coEvery { remoteDataSource.fetchUserLists(SESSION_ID) } returns dummyRemoteLists
        coEvery { remoteDataSource.fetchListItems(any()) } returns dummyItemsDto
        coJustRun { localDataSource.clearAllLists() }
        coJustRun { localDataSource.insertList(any()) }

        repository.refreshSavedLists()

        coVerify { localDataSource.clearAllLists() }
        coVerify(atLeast = 1) { localDataSource.insertList(any()) }
    }

    @Test
    fun `createSavedList inserts new list to local`() = runTest {
        coEvery { remoteDataSource.createList(SESSION_ID, "Watch-Later") } returns
                SavedListRemoteDto(id = LIST_ID, title = "Watch-Later")
        coJustRun { localDataSource.insertList(any()) }

        repository.createSavedList("Watch-Later")

        coVerify { localDataSource.insertList(match { it.id == LIST_ID }) }
    }

    @Test
    fun `deleteSavedList deletes from both remote and local`() = runTest {
        coEvery { remoteDataSource.deleteList(SESSION_ID, LIST_ID) } returns true
        coJustRun { localDataSource.deleteList(LIST_ID) }

        repository.deleteSavedList(LIST_ID)

        coVerify { remoteDataSource.deleteList(SESSION_ID, LIST_ID) }
        coVerify { localDataSource.deleteList(LIST_ID) }
    }

    @Test
    fun `clearAllLists calls local clear`() = runTest {
        coJustRun { localDataSource.clearAllLists() }

        repository.clearAllLists()

        coVerify { localDataSource.clearAllLists() }
    }

//    @Test
//    fun `getMoviesInList maps remote movies`() = runTest {
//        coEvery { remoteDataSource.fetchListItems(LIST_ID, PAGE) } returns dummyMoviesDto
//
//        val result = repository.getMoviesInList(LIST_ID, PAGE)
//
//        assertThat(result).isInstanceOf(List::class.java)
//        assertThat(result).hasSize(2)
//        assertThat(result[0]).isInstanceOf(Movie::class.java)
//        assertThat(result[0].id).isEqualTo(1)
//    }

    @Test
    fun `addMovieToList updates both remote and local on success`() = runTest {
        coEvery { remoteDataSource.addItem(SESSION_ID, LIST_ID, MOVIE_ID) } returns true
        coJustRun { localDataSource.addMovieToList(LIST_ID, MOVIE_ID) }

        repository.addMovieToList(LIST_ID, MOVIE_ID)

        coVerify { remoteDataSource.addItem(SESSION_ID, LIST_ID, MOVIE_ID) }
        coVerify { localDataSource.addMovieToList(LIST_ID, MOVIE_ID) }
    }

    @Test
    fun `removeMovieFromList updates both remote and local on success`() = runTest {
        coEvery { remoteDataSource.removeItem(SESSION_ID, LIST_ID, MOVIE_ID) } returns true
        coJustRun { localDataSource.removeMovieFromList(LIST_ID, MOVIE_ID) }

        repository.removeMovieFromList(LIST_ID, MOVIE_ID)

        coVerify { remoteDataSource.removeItem(SESSION_ID, LIST_ID, MOVIE_ID) }
        coVerify { localDataSource.removeMovieFromList(LIST_ID, MOVIE_ID) }
    }

    private companion object {
        const val SESSION_ID = "session-123"
        const val LIST_ID = 7
        const val MOVIE_ID = 99
        const val PAGE = 1
    }
}