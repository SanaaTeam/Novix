package com.sanaa.vod.media

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.remote.dto.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.media.actor.ActorApiService
import com.sanaa.vod.media.actor.RemoteActorDataSourceImpl
import com.sanaa.vod.media.actor.response.ActorCastCreditsResponse
import com.sanaa.vod.media.actor.response.ActorImagesResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoteActorDataSourceImplTest {
    lateinit var actorDataSource: RemoteActorDataSourceImpl
    private val actorApi = mockk<ActorApiService>()

    @BeforeEach
    fun setUp() {
        actorDataSource = RemoteActorDataSourceImpl(actorApi)
    }

    @Test
    fun `getActorDetails show return ActorDto `() = runTest {
        coEvery { actorApi.fetchActorDetails(1) } returns dummyActorDto

        val result = actorDataSource.getActorDetails(1)

        assertThat(result.id == dummyActorDto.id)

    }

    @Test
    fun `getActorImages show return List of ImageDto `() = runTest {
        coEvery { actorApi.fetchActorImages(1) } returns dummyActorImagesResponse

        val result = actorDataSource.getActorImages(1)

        assertThat(result.size == dummyActorImagesResponse.profiles.size)
    }

    @Test
    fun `getActorTopMovies show return List of ActorCastCreditDto `() = runTest {
        coEvery { actorApi.fetchActorTopMovies(1) } returns dummyActorTopMoviesResponse

        val result = actorDataSource.getActorTopMovies(1)

        assertThat(result.size == dummyActorTopMoviesResponse.cast.size)
    }

    @Test
    fun `getActorTopTvSeries show return List of ActorCastCreditDto `() = runTest {
        coEvery { actorApi.fetchActorTopTvSeries(1) } returns dummyActorTopTvShowsResponse

        val result = actorDataSource.getActorTopTvSeries(1)

        assertThat(result.size == dummyActorTopTvShowsResponse.cast.size)
    }


    private companion object {
        val dummyActorDto = ActorDto(
            id = 1,
            name = "name",
        )

        val dummyActorImagesResponse = ActorImagesResponse(
            id = 1,
            profiles = listOf(
                ImageDto(
                    filePath = "filePath",
                )
            )
        )
        val dummyActorTopMoviesResponse = ActorCastCreditsResponse(
            actorId = 1,
            cast = listOf(
                ActorCastCreditDto(
                    id = 1,
                    movieTitle = "name",
                )
            )
        )
        val dummyActorTopTvShowsResponse = ActorCastCreditsResponse(
            actorId = 1,
            cast = listOf(
                ActorCastCreditDto(
                    id = 1,
                    tvShowTitle = "name",
                )
            )
        )
    }
}