package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.dataSource.remote.actor.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.dto.ActorDto
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
    fun `getActorTopTvSeries throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorTopTvSeries(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> {
            repository.getActorTopTvSeries(1)
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
    fun `getActorTopTvSeries throws RetrievingDataFailureException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorTopTvSeries(any()) } throws Exception()

            assertThrows<RetrievingDataFailureException> {
                repository.getActorTopTvSeries(1)
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

    companion object {
        private val sampleActorDto = ActorDto(
            id = 1,
            name = "Tom Hanks",
            profileImagePath = "/path/to/profile.jpg"
        )
    }

}
