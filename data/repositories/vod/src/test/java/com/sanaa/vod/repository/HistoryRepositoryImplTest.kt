package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.local.history.LocalHistoryDataSource
import com.sanaa.vod.dataSource.local.history.dto.search.QueryLocalDto
import com.sanaa.vod.dataSource.local.history.dto.search.RecentViewedLocalDto
import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import com.sanaa.vod.repository.mapper.history.toDto
import com.sanaa.vod.repository.mapper.history.toEntity
import com.sanaa.vod.util.exceptions.ConnectionException
import entity.MediaHistoryItem
import exceptions.NoNetworkException
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import usecase.search.search_param.MediaType

class HistoryRepositoryImplTest {
    private lateinit var repository: HistoryRepositoryImpl
    private var localDataSource: LocalHistoryDataSource = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        repository = HistoryRepositoryImpl(localDataSource)
    }

    @Test
    fun `getSearchHistory returns list of search history`() = runTest {
        coEvery { localDataSource.getQueries(2) } returns flowOf(givenQueries)

        val expected = givenQueries.map { it.toEntity() }
        val result = repository.getSearchHistory(2)

        assertThat(result.first()).isEqualTo(expected)
    }

    @Test
    fun `getSearchHistory throws exception when failed to retrieve search history`() = runTest {
        coEvery { localDataSource.getQueries(2) } throws Exception()

        assertThrows<NovixAppException> { repository.getSearchHistory(2).first() }
    }

    @Test
    fun `addSearchHistory adds search history`() = runTest {
        val query = "query"
        coEvery { localDataSource.insertQuery(query) } returns Unit

        repository.addSearchHistory(query)

        coVerify { localDataSource.insertQuery(query) }
    }

    @Test
    fun `addSearchHistory throws exception when failed to add search history`() = runTest {
        val query = "query"
        coEvery { localDataSource.insertQuery(query) } throws Exception()

        val result = runCatching { repository.addSearchHistory(query) }

        assertThrows<NovixAppException> { result.getOrThrow() }
    }

    @Test
    fun `clearSearchHistory clears search history`() = runTest {
        coEvery { localDataSource.deleteAllQueries() } returns Unit

        repository.clearSearchHistory()

        coVerify { localDataSource.deleteAllQueries() }
    }

    @Test
    fun `clearSearchHistory throws exception when failed to clear search history`() = runTest {
        coEvery { localDataSource.deleteAllQueries() } throws Exception()

        val result = runCatching { repository.clearSearchHistory() }

        assertThrows<NovixAppException> { result.getOrThrow() }
    }

    @Test
    fun `removeSearchHistoryById removes search history by id`() = runTest {
        val id = 1
        coEvery { localDataSource.deleteQueryById(id) } returns Unit

        repository.removeSearchHistoryById(id)

        coVerify { localDataSource.deleteQueryById(id) }
    }

    @Test
    fun `removeSearchHistoryById throws exception when failed to remove search history`() =
        runTest {
            val id = 1
            coEvery { localDataSource.deleteQueryById(id) } throws Exception()

            val result = runCatching { repository.removeSearchHistoryById(id) }

            assertThrows<NovixAppException> { result.getOrThrow() }
        }

    @Test
    fun `getRecentViewed returns list of recent viewed`() = runTest {
        coEvery { localDataSource.getAllRecentViewed(2) } returns flowOf(givenRecentViewed)

        val expected = givenRecentViewed.map { it.toEntity() }
        val result = repository.getRecentViewed(2)

        assertThat(result.first()).isEqualTo(expected)
    }

    @Test
    fun `getRecentViewed throws exception when failed to retrieve recent viewed`() = runTest {
        coEvery { localDataSource.getAllRecentViewed(2) } throws Exception()

        assertThrows<NovixAppException> { repository.getRecentViewed(2).first() }
    }

    @Test
    fun `addRecentViewed adds recent viewed`() = runTest {
        val recentViewed = givenRecentViewed.first()
        coEvery { localDataSource.insertRecentViewed(recentViewed) } returns Unit

        repository.addRecentViewedMedia(recentViewed.toEntity())

        coVerify {
            localDataSource.insertRecentViewed(match {
                it.id == recentViewed.id &&
                        it.imageUrl == recentViewed.imageUrl &&
                        it.isSaved == recentViewed.isSaved &&
                        it.mediaType == recentViewed.mediaType
            })
        }
    }

    @Test
    fun `addRecentViewed throws exception when failed to add recent viewed`() = runTest {
        coEvery { localDataSource.insertRecentViewed(any()) } throws Exception()

        val result = runCatching {
            repository.addRecentViewedMedia(givenRecentViewed.first().toEntity())
        }
        assertThrows<NovixAppException> { result.getOrThrow() }
    }

    @Test
    fun `clearRecentViewed clears recent viewed`() = runTest {
        coEvery { localDataSource.deleteAllRecentViewed() } returns Unit

        repository.clearRecentViewed()

        coVerify { localDataSource.deleteAllRecentViewed() }
    }

    @Test
    fun `clearRecentViewed throws exception when failed to clear recent viewed`() = runTest {
        coEvery { localDataSource.deleteAllRecentViewed() } throws Exception()

        val result = runCatching { repository.clearRecentViewed() }

        assertThrows<NovixAppException> { result.getOrThrow() }
    }

    @Test
    fun `getSearchHistory throws NoNetworkException when ConnectionException occurs`() = runTest {
        coEvery { localDataSource.getQueries(any()) } throws ConnectionException()

        assertThrows<NoNetworkException> {
            repository.getSearchHistory(2).first()
        }
    }

    @Test
    fun `getRecentViewed throws NoNetworkException when ConnectionException occurs`() = runTest {
        coEvery { localDataSource.getAllRecentViewed(any()) } throws ConnectionException()

        assertThrows<NoNetworkException> {
            repository.getRecentViewed(2).first()
        }
    }

    @Test
    fun `addWatchedMediaHistory should call localDataSource insertWatchedMediaHistory with correct parameters`() =
        runTest {
            val watchedMedia = mediaItem.toDto("username")
            coEvery { localDataSource.insertWatchedMediaHistory(watchedMedia) } returns Unit

            repository.addWatchedMediaHistory(
                watchedMedia.username,
                mediaItem
            )

            coVerify {
                localDataSource.insertWatchedMediaHistory(match {
                    it.id == watchedMedia.id &&
                            it.posterImageUrl == watchedMedia.posterImageUrl &&
                            it.mediaType == watchedMedia.mediaType &&
                            it.username == watchedMedia.username &&
                            it.genres == watchedMedia.genres
                })
            }
        }

    @Test
    fun `addWatchedMediaHistory throws exception when failed to add watched media history`() =
        runTest {
            val watchedMedia = givenWatchedMedia.first()
            coEvery { localDataSource.insertWatchedMediaHistory(any()) } throws Exception()

            val result = runCatching {
                repository.addWatchedMediaHistory("", watchedMedia.toEntity())
            }

            assertThrows<NovixAppException> { result.getOrThrow() }
        }

    @Test
    fun `getWatchedMediaHistory returns list of watched list when available`() = runTest {
        val username = "username"
        val mediaType = MediaType.MOVIE
        val genreId: Int? = null
        coEvery {
            localDataSource.getWatchedMediaHistory(username, mediaType, genreId)
        } returns flowOf(givenWatchedMedia)

        val expected = givenWatchedMedia.map { it.toEntity() }
        val result = repository.getWatchedMediaHistory(username, mediaType, genreId)

        assertThat(result.first()).isEqualTo(expected)
    }

    @Test
    fun `getWatchedMediaHistory throws exception when failed to retrieve date`() = runTest {
        coEvery { localDataSource.getWatchedMediaHistory(any(), any(), any()) } throws Exception()

        assertThrows<NovixAppException> {
            repository.getWatchedMediaHistory(
                "",
                null,
                null
            ).first()
        }
    }


    private companion object {
        val givenRecentViewed = listOf(
            RecentViewedLocalDto(
                id = 1,
                imageUrl = "imageUrl1",
                isSaved = true,
                mediaType = MediaType.MOVIE.name,
            ),
            RecentViewedLocalDto(
                id = 2,
                imageUrl = "imageUrl2",
                isSaved = false,
                mediaType = MediaType.MOVIE.name,
            )
        )

        val givenQueries = listOf(
            QueryLocalDto(id = 1, query = "query1", timestamp = 1L),
            QueryLocalDto(id = 2, query = "query2", timestamp = 2L)
        )


        val givenWatchedMedia = listOf(
            WatchedMediaHistoryLocalDto(
                id = 1,
                posterImageUrl = "imageUrl1",
                mediaType = MediaType.MOVIE.name,
                username = "zack",
                genres = "11,12",
            ),
            WatchedMediaHistoryLocalDto(
                id = 2,
                posterImageUrl = "imageUrl2",
                mediaType = MediaType.TV_SHOW.name,
                username = "mark",
                genres = "15,20",
            )
        )

        val mediaItem = MediaHistoryItem(
            id = 1,
            posterImageUrl = "imageUrl1",
            mediaType = MediaType.MOVIE,
            genres = emptyList()
        )
    }
}