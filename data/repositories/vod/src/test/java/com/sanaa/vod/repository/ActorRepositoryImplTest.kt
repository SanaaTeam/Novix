package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.remote.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import com.sanaa.vod.repository.mapper.media.toEntity
import com.sanaa.vod.util.exceptions.ConnectionException
import exceptions.NoNetworkException
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ActorRepositoryImplTest {

    private lateinit var repository: ActorRepositoryImpl
    private val remoteDataSource: RemoteActorDataSource = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        repository = ActorRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getActorDetails returns expected Actor`() = runTest {
        coEvery { remoteDataSource.getActorDetails(1) } returns sampleActorDto

        val result = repository.getActorDetails(1)

        assertThat(result.name).isEqualTo("Tom Hanks")
    }

    @Test
    fun `getActorDetails throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remoteDataSource.getActorDetails(any()) } throws ConnectionException()

        assertThrows<NoNetworkException> {
            repository.getActorDetails(1)
        }
    }

    @Test
    fun `getActorDetails throws NovixAppException on Exception`() = runTest {
        coEvery { remoteDataSource.getActorDetails(any()) } throws RuntimeException()

        assertThrows<NovixAppException> {
            repository.getActorDetails(1)
        }
    }

    @Test
    fun `getGalleryImages throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorImages(any()) } throws ConnectionException()

        assertThrows<NoNetworkException> {
            repository.getGalleryImageUrls(1)
        }
    }

    @Test
    fun `getActorTopMovies throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorMovies(any()) } throws ConnectionException()

        assertThrows<NoNetworkException> {
            repository.getActorTopMovies(1)
        }
    }

    @Test
    fun `getActorTopTvShows throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorTvShows(any()) } throws ConnectionException()

        assertThrows<NoNetworkException> {
            repository.getActorTopTvShows(1)
        }
    }

    @Test
    fun `getProfileImages throws NovixAppException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorImages(any()) } throws Exception()

            assertThrows<NovixAppException> {
                repository.getProfileImageUrls(1, 1)
            }
        }

    @Test
    fun `getActorTopMovies throws NovixAppException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorMovies(any()) } throws Exception()

            assertThrows<NovixAppException> {
                repository.getActorTopMovies(1)
            }
        }

    @Test
    fun `getActorTopTvShows throws NovixAppException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorTvShows(any()) } throws Exception()

            assertThrows<NovixAppException> {
                repository.getActorTopTvShows(1)
            }
        }

    @Test
    fun `getProfileImages propagates NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remoteDataSource.getActorImages(any()) } throws ConnectionException()

        assertThrows<NoNetworkException> { repository.getProfileImageUrls(42, 1) }
    }

    @Test
    fun `getGalleryImages propagates NovixAppException on generic error`() = runTest {
        coEvery { remoteDataSource.getActorImages(any()) } throws IllegalStateException("boom")

        assertThrows<NovixAppException> { repository.getGalleryImageUrls(42) }
    }

    @Test
    fun `getActorTopMovies returns empty list when no data`() = runTest {
        coEvery { remoteDataSource.getActorMovies(any()) } returns emptyList()

        val result = repository.getActorTopMovies(1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getActorTopTvShows returns list of TV series`() = runTest {
        val tvDto = listOf(mockk<ActorCastCreditDto>(relaxed = true))
        coEvery { remoteDataSource.getActorTvShows(1) } returns tvDto

        val result = repository.getActorTopTvShows(1)

        assertThat(result).hasSize(1)
    }

    @Test
    fun `getActorTopMovies returns list of movies`() = runTest {
        val movieDto = listOf(mockk<ActorCastCreditDto>(relaxed = true))
        coEvery { remoteDataSource.getActorMovies(1) } returns movieDto

        val result = repository.getActorTopMovies(1)

        assertThat(result).hasSize(1)
    }

    @Test
    fun `getActorDetails wraps exception with custom error message`() = runTest {
        val error = RuntimeException("Something went wrong")
        coEvery { remoteDataSource.getActorDetails(1) } throws error

        val exception = assertThrows<NovixAppException> {
            repository.getActorDetails(1)
        }

        assertThat(exception.message).contains("Failed to retrieve actor details for ID: 1")
        assertThat(exception.message).contains("Something went wrong")
    }

    @Test
    fun `getProfileImageUrls returns mapped image URLs`() = runTest {
        val imageDto = listOf(mockk<ImageDto>(relaxed = true), mockk(relaxed = true))
        coEvery { remoteDataSource.getActorImages(1) } returns imageDto

        val result = repository.getProfileImageUrls(1, 2)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `getGalleryImageUrls returns mapped image URLs successfully`() = runTest {
        val sampleDtoList = listOf(
            ImageDto(filePath = "/img1.jpg"),
            ImageDto(filePath = "/img2.jpg")
        )

        coEvery { remoteDataSource.getActorImages(1) } returns sampleDtoList

        val result = repository.getGalleryImageUrls(1)

        assertThat(result).containsExactly(
            "https://image.tmdb.org/t/p/w500/img1.jpg",
            "https://image.tmdb.org/t/p/w500/img2.jpg"
        )
    }

    @Test
    fun `getTrendingActors returns empty list when there is no data available`() = runTest {
        coEvery { remoteDataSource.fetchTrendingPeople(any()) } returns emptyList()

        val result = repository.getTrendingActors(1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTrendingActors returns data when available`() = runTest {
        val page = 1
        val actorList = listOf(sampleActorDto)
        coEvery { remoteDataSource.fetchTrendingPeople(page) } returns actorList

        val result = repository.getTrendingActors(page)

        assertThat(result).isEqualTo(actorList.map { it.toEntity() })
    }

    companion object {
        private val sampleActorDto = ActorDto(
            id = 1,
            name = "Tom Hanks",
            profileImagePath = "/path/to/profile.jpg"
        )
    }
}