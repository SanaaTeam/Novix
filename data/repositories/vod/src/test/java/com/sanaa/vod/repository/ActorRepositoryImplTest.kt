package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.remote.actor.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.dto.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.UnknownHostException

class ActorRepositoryImplTest {

    private lateinit var repository: ActorRepositoryImpl
    private val remoteDataSource: RemoteActorDataSource = mockk(relaxed = true)
    private val languageProvider: LanguageProvider = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        repository = ActorRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getActorDetails returns expected Actor`() = runTest {
        coEvery { remoteDataSource.getActorDetails(1) } returns sampleActorDto
        coEvery { languageProvider.getCurrentLanguage() } returns "en"

        val result = repository.getActorDetails(1)

        assertThat(result.name).isEqualTo("Tom Hanks")
        assertThat(result.region).isEqualTo(null)
    }

    @Test
    fun `getActorDetails throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remoteDataSource.getActorDetails(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> {
            repository.getActorDetails(1)
        }
    }

    @Test
    fun `getActorDetails throws RetrievingDataFailureException on Exception`() = runTest {
        coEvery { remoteDataSource.getActorDetails(any()) } throws RuntimeException()

        assertThrows<RetrievingDataFailureException> {
            repository.getActorDetails(1)
        }
    }


    @Test
    fun `getGalleryImages throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorImages(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> {
            repository.getGalleryImageUrls(1)
        }
    }

    @Test
    fun `getActorTopMovies throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorTopMovies(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> {
            repository.getActorTopMovies(1)
        }
    }

    @Test
    fun `getActorTopTvShows throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorTopTvShows(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> {
            repository.getActorTopTvShows(1)
        }
    }

    @Test
    fun `getProfileImages throws RetrievingDataFailureException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorImages(any()) } throws Exception()

            assertThrows<RetrievingDataFailureException> {
                repository.getProfileImageUrls(1, 1)
            }
        }

    @Test
    fun `getActorTopMovies throws RetrievingDataFailureException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorTopMovies(any()) } throws Exception()

            assertThrows<RetrievingDataFailureException> {
                repository.getActorTopMovies(1)
            }
        }

    @Test
    fun `getActorTopTvShows throws RetrievingDataFailureException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorTopTvShows(any()) } throws Exception()

            assertThrows<RetrievingDataFailureException> {
                repository.getActorTopTvShows(1)
            }
        }

    @Test
    fun `getProfileImages propagates NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remoteDataSource.getActorImages(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> { repository.getProfileImageUrls(42, 1) }
    }

    @Test
    fun `getGalleryImages propagates RetrievingDataFailureException on generic error`() = runTest {
        coEvery { remoteDataSource.getActorImages(any()) } throws IllegalStateException("boom")

        assertThrows<RetrievingDataFailureException> { repository.getGalleryImageUrls(42) }
    }

    @Test
    fun `getActorTopMovies returns empty list when no data`() = runTest {
        coEvery { remoteDataSource.getActorTopMovies(any()) } returns emptyList()

        val result = repository.getActorTopMovies(1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getActorTopTvShows returns list of TV series`() = runTest {
        val tvDtos = listOf(mockk<ActorCastCreditDto>(relaxed = true))
        coEvery { remoteDataSource.getActorTopTvShows(1) } returns tvDtos

        val result = repository.getActorTopTvShows(1)

        assertThat(result).hasSize(1)
    }

    @Test
    fun `getActorTopMovies returns list of movies`() = runTest {
        val movieDtos = listOf(mockk<ActorCastCreditDto>(relaxed = true))
        coEvery { remoteDataSource.getActorTopMovies(1) } returns movieDtos

        val result = repository.getActorTopMovies(1)

        assertThat(result).hasSize(1)
    }

    @Test
    fun `getActorDetails wraps exception with custom error message`() = runTest {
        val error = RuntimeException("Something went wrong")
        coEvery { remoteDataSource.getActorDetails(1) } throws error

        val exception = assertThrows<RetrievingDataFailureException> {
            repository.getActorDetails(1)
        }

        assertThat(exception.message).contains("Failed to retrieve actor details for ID: 1")
        assertThat(exception.message).contains("Something went wrong")
    }

    @Test
    fun `getProfileImageUrls returns mapped image URLs`() = runTest {
        val imageDtos = listOf(mockk<ImageDto>(relaxed = true), mockk(relaxed = true))
        coEvery { remoteDataSource.getActorImages(1) } returns imageDtos

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

    companion object {
        private val sampleActorDto = ActorDto(
            id = 1,
            name = "Tom Hanks",
            profileImagePath = "/path/to/profile.jpg"
        )
    }

}
