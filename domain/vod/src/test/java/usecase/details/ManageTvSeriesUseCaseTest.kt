package usecase.details

import com.google.common.truth.Truth.assertThat
import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.TvSeriesRepository
import usecase.ManageTvSeriesUseCase

class ManageTvSeriesUseCaseTest {

    private val tvSeriesRepository: TvSeriesRepository = mockk(relaxed = true)
    private lateinit var manageTvSeriesDetailsUseCase: ManageTvSeriesUseCase

    @BeforeEach
    fun setUp() {
        manageTvSeriesDetailsUseCase = ManageTvSeriesUseCase(tvSeriesRepository)
    }

    @Test
    fun `getTvSeriesByGenre should return list when available`() = runTest {

        coEvery { tvSeriesRepository.getTvSeriesByGenre(1, dummyGenre.id) } returns listOf(
            dummyTvSeries
        )
        val result = manageTvSeriesDetailsUseCase.getTvSeriesByGenre(1, dummyGenre.id)
        assertThat(result).isEqualTo(listOf(dummyTvSeries))
    }

    @Test
    fun `getTvSeriesByGenre should return empty list when none available`() = runTest {

        coEvery { tvSeriesRepository.getTvSeriesByGenre(1, dummyGenre.id) } returns emptyList()

        val result = manageTvSeriesDetailsUseCase.getTvSeriesByGenre(1, dummyGenre.id)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvSeriesByGenre should throw when repository fails`() = runTest {
        coEvery {
            tvSeriesRepository.getTvSeriesByGenre(
                1,
                dummyGenre.id
            )
        } throws NovixAppException(
            "Error"
        )
        assertThrows<NovixAppException> {

            manageTvSeriesDetailsUseCase.getTvSeriesByGenre(1, dummyGenre.id)
        }
    }

    @Test
    fun `getTvSeriesDetails should return details when available`() = runTest {
        val seriesId = 1
        coEvery { tvSeriesRepository.getTvSeriesDetails(seriesId) } returns dummyTvSeries

        val result = manageTvSeriesDetailsUseCase.getTvSeriesDetails(seriesId)

        assertThat(result).isEqualTo(dummyTvSeries)
    }

    @Test
    fun `getTvSeriesDetails should throw NovixAppException when not found`() = runTest {
        val seriesId = 1
        coEvery { tvSeriesRepository.getTvSeriesDetails(seriesId) } throws NovixAppException("Not found")

        assertThrows<NovixAppException> {
            manageTvSeriesDetailsUseCase.getTvSeriesDetails(seriesId)
        }
    }

    @Test
    fun `getTvSeriesCast should return cast list when available`() = runTest {
        val seriesId = 2
        val expected = listOf(mockk<Actor>(), mockk<Actor>())
        coEvery { tvSeriesRepository.getTvSeriesCast(seriesId) } returns expected

        val result = manageTvSeriesDetailsUseCase.getTvSeriesCast(seriesId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvSeriesCast should return empty list when none available`() = runTest {
        val seriesId = 2
        coEvery { tvSeriesRepository.getTvSeriesCast(seriesId) } returns emptyList()

        val result = manageTvSeriesDetailsUseCase.getTvSeriesCast(seriesId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvSeriesCast should throw when repository fails`() = runTest {
        val seriesId = 2
        coEvery { tvSeriesRepository.getTvSeriesCast(seriesId) } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvSeriesDetailsUseCase.getTvSeriesCast(seriesId)
        }
    }

    @Test
    fun `getTvSeriesImages should return images when available`() = runTest {
        val seriesId = 3
        val expected = listOf("img1.jpg", "img2.jpg")

        coEvery { tvSeriesRepository.getTvSeriesImageUrls(seriesId, any()) } returns expected

        val result = manageTvSeriesDetailsUseCase.getTvSeriesImages(seriesId)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvSeriesImages should return empty list when none available`() = runTest {
        val seriesId = 3
        coEvery { tvSeriesRepository.getTvSeriesImageUrls(seriesId, 10) } returns emptyList()

        val result = manageTvSeriesDetailsUseCase.getTvSeriesImages(seriesId)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvSeriesImages should throw when repository fails`() = runTest {
        val seriesId = 3
        coEvery {
            tvSeriesRepository.getTvSeriesImageUrls(
                seriesId,
                any()
            )
        } throws NovixAppException("Not found")
        assertThrows<NovixAppException> {
            manageTvSeriesDetailsUseCase.getTvSeriesImages(seriesId)
        }
    }

    @Test
    fun `getTvSeriesReviews should return reviews when available`() = runTest {
        val seriesId = 4
        val expected = listOf(mockk<Review>(), mockk<Review>())

        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) } returns expected

        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)

        coVerify { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) }
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvSeriesReviews should return empty list when none available`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) } returns emptyList()

        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvSeriesReviews should throw when repository fails`() = runTest {
        val seriesId = 4
        coEvery {
            tvSeriesRepository.getTvSeriesReviews(
                seriesId,
                1
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)
        }
    }

    @Test
    fun `getTvSeriesSeasonDetails should return season when available`() = runTest {
        val seriesId = 5
        val seasonNumber = 1
        val expected = mockk<Season>()
        coEvery { tvSeriesRepository.getTvSeriesSeason(seriesId, seasonNumber) } returns expected

        val result = manageTvSeriesDetailsUseCase.getTvSeriesSeasonDetails(seriesId, seasonNumber)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvSeriesSeasonDetails should throw when repository fails`() = runTest {
        val seriesId = 5
        val seasonNumber = 1
        coEvery {
            tvSeriesRepository.getTvSeriesSeason(
                seriesId,
                seasonNumber
            )
        } throws IllegalStateException("Error")
        assertThrows<IllegalStateException> {
            manageTvSeriesDetailsUseCase.getTvSeriesSeasonDetails(seriesId, seasonNumber)
        }
    }

    @Test
    fun `getTvSeriesTrailer should return trailer url when available`() = runTest {
        val seriesId = 6
        val trailerUrl = "trailer.mp4"
        coEvery { tvSeriesRepository.getTvSeriesTrailer(seriesId) } returns trailerUrl
        val result = manageTvSeriesDetailsUseCase.getTvSeriesTrailer(seriesId)
        assertThat(result).isEqualTo(trailerUrl)
    }

    @Test
    fun `getTvSeriesTrailer should return null when none available`() = runTest {
        val seriesId = 6
        coEvery { tvSeriesRepository.getTvSeriesTrailer(seriesId) } returns null
        val result = manageTvSeriesDetailsUseCase.getTvSeriesTrailer(seriesId)
        assertThat(result).isNull()
    }

    @Test
    fun `getTvSeriesTrailer should throw when repository fails`() = runTest {
        val seriesId = 6
        coEvery { tvSeriesRepository.getTvSeriesTrailer(seriesId) } throws IllegalStateException("Error")
        assertThrows<IllegalStateException> {
            manageTvSeriesDetailsUseCase.getTvSeriesTrailer(seriesId)
        }
    }

    @Test
    fun `getPopularSeries should return series when available`() = runTest {
        val expected = listOf(dummyTvSeries)
        coEvery { tvSeriesRepository.getPopularSeries(1) } returns expected

        val result = manageTvSeriesDetailsUseCase.getPopularSeries(1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getPopularSeries should throw when repository fails`() = runTest {
        coEvery {
            tvSeriesRepository.getPopularSeries(1)
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvSeriesDetailsUseCase.getPopularSeries(1)
        }
    }

    @Test
    fun `getTopRatedTvSeries should return series when available`() = runTest {
        val expected = listOf(dummyTvSeries, dummyTvSeries.copy(id = 2))
        coEvery { tvSeriesRepository.getTopRatedTvSeries(1, null) } returns expected

        val result = manageTvSeriesDetailsUseCase.getTopRatedTvSeries(1, null)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTopRatedTvSeries should throw when repository fails`() = runTest {
        coEvery {
            tvSeriesRepository.getTopRatedTvSeries(
                1,
                null
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvSeriesDetailsUseCase.getTopRatedTvSeries(1, null)
        }
    }

    @Test
    fun `getTrendingTvSeries should return series when available`() = runTest {
        val expected = listOf(dummyTvSeries)
        coEvery { tvSeriesRepository.getTrendingTvSeries(1, dummyGenre.id) } returns expected

        val result = manageTvSeriesDetailsUseCase.getTrendingTvSeries(1, dummyGenre.id)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTrendingTvSeries should throw when repository fails`() = runTest {
        coEvery {
            tvSeriesRepository.getTrendingTvSeries(
                1,
                dummyGenre.id
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvSeriesDetailsUseCase.getTrendingTvSeries(1, dummyGenre.id)
        }
    }

    @Test
    fun `getSeriesGenres should return genres when available`() = runTest {
        val expected = listOf(dummyGenre)
        coEvery { tvSeriesRepository.getSeriesGenres() } returns expected

        val result = manageTvSeriesDetailsUseCase.getSeriesGenres()

        assertThat(result).isEqualTo(expected)
    }


    @Test
    fun `getSeriesGenres should throw when repository fails`() = runTest {
        coEvery { tvSeriesRepository.getSeriesGenres() } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvSeriesDetailsUseCase.getSeriesGenres()
        }
    }

    @Test
    fun `getTvSeriesSeasonDetails should return correct season title`() = runTest {
        val seriesId = 10
        val seasonNumber = 2
        coEvery { tvSeriesRepository.getTvSeriesSeason(seriesId, seasonNumber) } returns dummySeason

        val result = manageTvSeriesDetailsUseCase.getTvSeriesSeasonDetails(seriesId, seasonNumber)

        assertThat(result.title).isEqualTo("Season 2")
    }

    @Test
    fun `getTvSeriesSeasonDetails should return correct number of episodes`() = runTest {
        val seriesId = 10
        val seasonNumber = 2
        coEvery { tvSeriesRepository.getTvSeriesSeason(seriesId, seasonNumber) } returns dummySeason

        val result = manageTvSeriesDetailsUseCase.getTvSeriesSeasonDetails(seriesId, seasonNumber)

        assertThat(result.episodes.size).isEqualTo(2)
    }

    @Test
    fun `getTvSeriesSeasonDetails should return correct title for first episode`() = runTest {
        val seriesId = 10
        val seasonNumber = 2
        coEvery { tvSeriesRepository.getTvSeriesSeason(seriesId, seasonNumber) } returns dummySeason

        val result = manageTvSeriesDetailsUseCase.getTvSeriesSeasonDetails(seriesId, seasonNumber)

        assertThat(result.episodes[0].title).isEqualTo("Episode 1")
    }

    @Test
    fun `getTvSeriesSeasonDetails should return correct imdbRating for second episode`() = runTest {
        val seriesId = 10
        val seasonNumber = 2
        coEvery { tvSeriesRepository.getTvSeriesSeason(seriesId, seasonNumber) } returns dummySeason

        val result = manageTvSeriesDetailsUseCase.getTvSeriesSeasonDetails(seriesId, seasonNumber)

        assertThat(result.episodes[1].imdbRating).isEqualTo(8.7f)
    }

    @Test
    fun `getTvSeriesReviews should return correct review id`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)

        assertThat(result[0].id).isEqualTo("rev123")
    }

    @Test
    fun `getTvSeriesReviews should return correct authorName`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)

        assertThat(result[0].authorName).isEqualTo("John Doe")
    }

    @Test
    fun `getTvSeriesReviews should return correct userHandle`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)

        assertThat(result[0].userHandle).isEqualTo("@johnny")
    }

    @Test
    fun `getTvSeriesReviews should return correct avatarUrl`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)

        assertThat(result[0].avatarUrl).isEqualTo("https://avatar.com/john.jpg")
    }

    @Test
    fun `getTvSeriesReviews should return correct rating`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)

        assertThat(result[0].rating).isEqualTo(4.5f)
    }

    @Test
    fun `getTvSeriesReviews should return correct content`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)

        assertThat(result[0].content).isEqualTo("Amazing series, highly recommended!")
    }

    @Test
    fun `getTvSeriesReviews should return correct createdDate`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId, 1)

        assertThat(result[0].createdDate).isEqualTo(LocalDate(2023, 5, 20))
    }
    @Test
    fun `getUserRatedTvSeries should return rated tv series when available`() = runTest {
        val accountId = 123L
        val sessionId = "session_abc"
        val expected = listOf(mockk<TvSeries>(), mockk<TvSeries>())
        coEvery { tvSeriesRepository.getUserRatedTvSeries(accountId, sessionId) } returns expected

        val result = manageTvSeriesDetailsUseCase.getUserRatedTvSeries(accountId, sessionId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getUserRatedTvSeries should return empty list when none rated`() = runTest {
        val accountId = 123L
        val sessionId = "session_abc"
        coEvery { tvSeriesRepository.getUserRatedTvSeries(accountId, sessionId) } returns emptyList()

        val result = manageTvSeriesDetailsUseCase.getUserRatedTvSeries(accountId, sessionId)

        assertThat(result).isEmpty()
    }


    @Test
    fun `deleteTvSeriesRate should return true when deletion is successful`() = runTest {
        val seriesId = 10
        coEvery { tvSeriesRepository.deleteTvSeriesRate(seriesId) } returns true

        val result = manageTvSeriesDetailsUseCase.deleteTvSeriesRate(seriesId)

        assertThat(result).isTrue()
    }

    @Test
    fun `deleteTvSeriesRate should return false when deletion fails`() = runTest {
        val seriesId = 11
        coEvery { tvSeriesRepository.deleteTvSeriesRate(seriesId) } returns false

        val result = manageTvSeriesDetailsUseCase.deleteTvSeriesRate(seriesId)

        assertThat(result).isFalse()
    }

    companion object {
        val dummyGenre = Genre(
            id = 1, name = "Action"
        )
        private val dummyEpisodes = listOf(
            Episode(
                id = 101,
                title = "Episode 1",
                number = 1,
                seasonNumber = 2,
                imdbRating = 8.5f,
                overview = "First episode overview",
                durationMinutes = 45,
                releaseDate = LocalDate(2023, 1, 1),
                stillImagePath = "image1.jpg",
                rating = 0
            ),
            Episode(
                id = 102,
                title = "Episode 2",
                number = 2,
                seasonNumber = 2,
                imdbRating = 8.7f,
                overview = "Second episode overview",
                durationMinutes = 50,
                releaseDate = LocalDate(2023, 1, 8),
                stillImagePath = "image2.jpg",
                rating = 0
            )
        )

        private val dummySeason = Season(
            id = 20,
            title = "Season 2",
            overview = "Second season overview",
            number = 2,
            episodes = dummyEpisodes
        )
        private val dummyTvSeries = TvSeries(
            id = 1,
            title = "The Walking Dead",
            overview = "",
            releaseDate = LocalDate(2021, 9, 10),
            genres = listOf(dummyGenre),
            imdbRating = 1.2f,
            posterImageUrl = "Image",
            seasonsCount = 5,
            rating = 0
        )
        private val dummyReview = Review(
            id = "rev123",
            authorName = "John Doe",
            userHandle = "@johnny",
            avatarUrl = "https://avatar.com/john.jpg",
            rating = 4.5f,
            content = "Amazing series, highly recommended!",
            createdDate = LocalDate(2023, 5, 20)
        )
    }

}