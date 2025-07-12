package com.sanaa.search.repository

import com.example.env_config.service.LanguageProvider
import com.google.common.truth.Truth.assertThat
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import com.sanaa.search.mapper.toSearchOutput
import com.sanaa.search.dataSource.remote.response.SearchResponse
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.UnknownHostException

class SearchRepositoryImplTest {

    private lateinit var searchRepository: SearchRepositoryImpl
    private val remoteDataSource: SearchRemoteDataSource = mockk(relaxed = true)
    private val localCacheSearchDataSource: LocalCacheSearchDataSource = mockk(relaxed = true)
    private val languageProvider: LanguageProvider = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        searchRepository =
            SearchRepositoryImpl(remoteDataSource, localCacheSearchDataSource, languageProvider)
    }

    @Test
    fun `searchActors returns cached actors when available`() = runTest {
        // Given
        val query = "Tom"
        coEvery { localCacheSearchDataSource.getActorsByQuery(query) } returns ActorsLocalDtoList

        // When
        val expected = ActorsLocalDtoList.map { it.toSearchOutput() }
        val result = searchRepository.searchActors(query)

        // Then
        assertThat(result).isEqualTo(expected)
    }


    @Test
    fun `searchActors fetches remote and caches when no local`() = runTest {
        val query = "Jane"
        coEvery { localCacheSearchDataSource.getActorsByQuery(query) } returns emptyList()
        coEvery { remoteDataSource.searchActors(query) } returns actorSearchResponse
        coEvery { localCacheSearchDataSource.cacheActor(any()) } just Runs

        searchRepository.searchActors(query)

        coVerify { remoteDataSource.searchActors(query) }
    }


    @Test
    fun `searchActors throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { localCacheSearchDataSource.getActorsByQuery(any()) } throws UnknownHostException()
        assertThrows<NoNetworkException> {
            searchRepository.searchActors("x")
        }
    }

    @Test
    fun `searchActors transforms other exceptions`() = runTest {
        coEvery { localCacheSearchDataSource.getActorsByQuery(any()) } throws Exception("oops")
        assertThrows<RetrievingDataFailureException> {
            searchRepository.searchActors("x")
        }
    }

    companion object {
        private val ActorsLocalDtoList = listOf(
            ActorsLocalDto(id = 1, name = "Tom Hanks", imagePath = "img", language = "en"),
            ActorsLocalDto(id = 2, name = "Leonardo DiCaprio", imagePath = "img", language = "en")
        )
        private val ActorsRemoteDtoList = listOf(
            ActorSearchDto(
                id = 1,
                name = "Tom Hanks",
                profileImagePath = "/path/to/image"
            ),
            ActorSearchDto(
                id = 2,
                name = "sam",
                profileImagePath = "/path/to/image"
            ),
        )
        private val actorSearchResponse = SearchResponse(
            page = 1,
            results = ActorsRemoteDtoList,
            totalPages = 1,
            totalResults = ActorsRemoteDtoList.size
        )
    }
}