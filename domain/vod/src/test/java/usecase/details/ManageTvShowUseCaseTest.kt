package usecase.details

import com.google.common.truth.Truth.assertThat
import entity.Actor
import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvShow
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.TvShowRepository
import usecase.ManageTvShowUseCase

class ManageTvShowUseCaseTest {

    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private lateinit var manageTvShowUseCase: ManageTvShowUseCase

    @BeforeEach
    fun setUp() {
        manageTvShowUseCase = ManageTvShowUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowsByGenre should return list when available`() = runTest {

        coEvery { tvShowRepository.getTvShowsByGenre(1, dummyGenre.id) } returns listOf(
            dummyTvShow
        )
        val result = manageTvShowUseCase.getTvShowsByGenre(1, dummyGenre.id)
        assertThat(result).isEqualTo(listOf(dummyTvShow))
    }

    @Test
    fun `getTvShowsByGenre should return empty list when none available`() = runTest {

        coEvery { tvShowRepository.getTvShowsByGenre(1, dummyGenre.id) } returns emptyList()

        val result = manageTvShowUseCase.getTvShowsByGenre(1, dummyGenre.id)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowsByGenre should throw when repository fails`() = runTest {
        coEvery {
            tvShowRepository.getTvShowsByGenre(
                1,
                dummyGenre.id
            )
        } throws NovixAppException(
            "Error"
        )
        assertThrows<NovixAppException> {

            manageTvShowUseCase.getTvShowsByGenre(1, dummyGenre.id)
        }
    }

    @Test
    fun `getTvShowDetails should return details when available`() = runTest {
        val seriesId = 1
        coEvery { tvShowRepository.getTvShowDetails(seriesId) } returns dummyTvShow

        val result = manageTvShowUseCase.getTvShowDetails(seriesId)

        assertThat(result).isEqualTo(dummyTvShow)
    }

    @Test
    fun `getTvShowDetails should throw NovixAppException when not found`() = runTest {
        val seriesId = 1
        coEvery { tvShowRepository.getTvShowDetails(seriesId) } throws NovixAppException("Not found")

        assertThrows<NovixAppException> {
            manageTvShowUseCase.getTvShowDetails(seriesId)
        }
    }

    @Test
    fun `getTvShowCast should return cast list when available`() = runTest {
        val seriesId = 2
        val expected = listOf(mockk<Actor>(), mockk<Actor>())
        coEvery { tvShowRepository.getTvShowCast(seriesId) } returns expected

        val result = manageTvShowUseCase.getTvShowCast(seriesId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvShowCast should return empty list when none available`() = runTest {
        val seriesId = 2
        coEvery { tvShowRepository.getTvShowCast(seriesId) } returns emptyList()

        val result = manageTvShowUseCase.getTvShowCast(seriesId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowCast should throw when repository fails`() = runTest {
        val seriesId = 2
        coEvery { tvShowRepository.getTvShowCast(seriesId) } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvShowUseCase.getTvShowCast(seriesId)
        }
    }

    @Test
    fun `getTvShowImageUrls should return images when available`() = runTest {
        val seriesId = 3
        val expected = listOf("img1.jpg", "img2.jpg")

        coEvery { tvShowRepository.getTvShowImageUrls(seriesId, any()) } returns expected

        val result = manageTvShowUseCase.getTvShowImageUrls(seriesId)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvShowImageUrls should return empty list when none available`() = runTest {
        val seriesId = 3
        coEvery { tvShowRepository.getTvShowImageUrls(seriesId, 10) } returns emptyList()

        val result = manageTvShowUseCase.getTvShowImageUrls(seriesId)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowImageUrls should throw when repository fails`() = runTest {
        val seriesId = 3
        coEvery {
            tvShowRepository.getTvShowImageUrls(
                seriesId,
                any()
            )
        } throws NovixAppException("Not found")
        assertThrows<NovixAppException> {
            manageTvShowUseCase.getTvShowImageUrls(seriesId)
        }
    }

    @Test
    fun `getTvShowReviews should return reviews when available`() = runTest {
        val seriesId = 4
        val expected = listOf(mockk<Review>(), mockk<Review>())

        coEvery { tvShowRepository.getTvShowReviews(seriesId, 1) } returns expected

        val result = manageTvShowUseCase.getTvShowReviews(seriesId, 1)

        coVerify { tvShowRepository.getTvShowReviews(seriesId, 1) }
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvShowReviews should return empty list when none available`() = runTest {
        val seriesId = 4
        coEvery { tvShowRepository.getTvShowReviews(seriesId, 1) } returns emptyList()

        val result = manageTvShowUseCase.getTvShowReviews(seriesId, 1)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowReviews should throw when repository fails`() = runTest {
        val seriesId = 4
        coEvery {
            tvShowRepository.getTvShowReviews(
                seriesId,
                1
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvShowUseCase.getTvShowReviews(seriesId, 1)
        }
    }

    @Test
    fun `getTvShowSeason should return season when available`() = runTest {
        val seriesId = 5
        val seasonNumber = 1
        val expected = mockk<Season>()
        coEvery { tvShowRepository.getTvShowSeason(seriesId, seasonNumber) } returns expected

        val result = manageTvShowUseCase.getTvShowSeasonDetails(seriesId, seasonNumber)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvShowSeason should throw when repository fails`() = runTest {
        val seriesId = 5
        val seasonNumber = 1
        coEvery {
            tvShowRepository.getTvShowSeason(
                seriesId,
                seasonNumber
            )
        } throws IllegalStateException("Error")
        assertThrows<IllegalStateException> {
            manageTvShowUseCase.getTvShowSeasonDetails(seriesId, seasonNumber)
        }
    }

    @Test
    fun `getTvShowTrailer should return trailer url when available`() = runTest {
        val seriesId = 6
        val trailerUrl = "trailer.mp4"
        coEvery { tvShowRepository.getTvShowTrailer(seriesId) } returns trailerUrl
        val result = manageTvShowUseCase.getTvShowTrailer(seriesId)
        assertThat(result).isEqualTo(trailerUrl)
    }

    @Test
    fun `getTvShowTrailer should return null when none available`() = runTest {
        val seriesId = 6
        coEvery { tvShowRepository.getTvShowTrailer(seriesId) } returns null
        val result = manageTvShowUseCase.getTvShowTrailer(seriesId)
        assertThat(result).isNull()
    }

    @Test
    fun `getTvShowTrailer should throw when repository fails`() = runTest {
        val seriesId = 6
        coEvery { tvShowRepository.getTvShowTrailer(seriesId) } throws IllegalStateException("Error")
        assertThrows<IllegalStateException> {
            manageTvShowUseCase.getTvShowTrailer(seriesId)
        }
    }

    @Test
    fun `getPopularTvShows should return series when available`() = runTest {
        val expected = listOf(dummyTvShow)
        coEvery { tvShowRepository.getPopularTvShows(1) } returns expected

        val result = manageTvShowUseCase.getPopularTvShows(1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getPopularTvShows should throw when repository fails`() = runTest {
        coEvery {
            tvShowRepository.getPopularTvShows(1)
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvShowUseCase.getPopularTvShows(1)
        }
    }

    @Test
    fun `getTopRatedTvShows should return series when available`() = runTest {
        val expected = listOf(dummyTvShow, dummyTvShow.copy(id = 2))
        coEvery { tvShowRepository.getTopRatedTvShows(1, null) } returns expected

        val result = manageTvShowUseCase.getTopRatedTvShows(1, null)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTopRatedTvShows should throw when repository fails`() = runTest {
        coEvery {
            tvShowRepository.getTopRatedTvShows(
                1,
                null
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvShowUseCase.getTopRatedTvShows(1, null)
        }
    }

    @Test
    fun `getTrendingTvShows should return series when available`() = runTest {
        val expected = listOf(dummyTvShow)
        coEvery { tvShowRepository.getTrendingTvShows(1, dummyGenre.id) } returns expected

        val result = manageTvShowUseCase.getTrendingTvShows(1, dummyGenre.id)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTrendingTvShows should throw when repository fails`() = runTest {
        coEvery {
            tvShowRepository.getTrendingTvShows(
                1,
                dummyGenre.id
            )
        } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvShowUseCase.getTrendingTvShows(1, dummyGenre.id)
        }
    }

    @Test
    fun `getTvShowGenres should return genres when available`() = runTest {
        val expected = listOf(dummyGenre)
        coEvery { tvShowRepository.getTvShowGenres() } returns expected

        val result = manageTvShowUseCase.getTvShowGenres()

        assertThat(result).isEqualTo(expected)
    }


    @Test
    fun `getTvShowGenres should throw when repository fails`() = runTest {
        coEvery { tvShowRepository.getTvShowGenres() } throws NovixAppException("Error")

        assertThrows<NovixAppException> {
            manageTvShowUseCase.getTvShowGenres()
        }
    }

    @Test
    fun `getTvShowSeason should return correct season title`() = runTest {
        val seriesId = 10
        val seasonNumber = 2
        coEvery { tvShowRepository.getTvShowSeason(seriesId, seasonNumber) } returns dummySeason

        val result = manageTvShowUseCase.getTvShowSeasonDetails(seriesId, seasonNumber)

        assertThat(result.title).isEqualTo("Season 2")
    }

    @Test
    fun `getTvShowSeason should return correct number of episodes`() = runTest {
        val seriesId = 10
        val seasonNumber = 2
        coEvery { tvShowRepository.getTvShowSeason(seriesId, seasonNumber) } returns dummySeason

        val result = manageTvShowUseCase.getTvShowSeasonDetails(seriesId, seasonNumber)

        assertThat(result.episodes.size).isEqualTo(2)
    }

    @Test
    fun `getTvShowSeason should return correct title for first episode`() = runTest {
        val seriesId = 10
        val seasonNumber = 2
        coEvery { tvShowRepository.getTvShowSeason(seriesId, seasonNumber) } returns dummySeason

        val result = manageTvShowUseCase.getTvShowSeasonDetails(seriesId, seasonNumber)

        assertThat(result.episodes[0].title).isEqualTo("Episode 1")
    }

    @Test
    fun `getTvShowSeason should return correct imdbRating for second episode`() = runTest {
        val seriesId = 10
        val seasonNumber = 2
        coEvery { tvShowRepository.getTvShowSeason(seriesId, seasonNumber) } returns dummySeason

        val result = manageTvShowUseCase.getTvShowSeasonDetails(seriesId, seasonNumber)

        assertThat(result.episodes[1].imdbRating).isEqualTo(8.7f)
    }

    @Test
    fun `getTvShowReviews should return correct review id`() = runTest {
        val seriesId = 4
        coEvery { tvShowRepository.getTvShowReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvShowUseCase.getTvShowReviews(seriesId, 1)

        assertThat(result[0].id).isEqualTo("rev123")
    }

    @Test
    fun `getTvShowReviews should return correct authorName`() = runTest {
        val seriesId = 4
        coEvery { tvShowRepository.getTvShowReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvShowUseCase.getTvShowReviews(seriesId, 1)

        assertThat(result[0].authorName).isEqualTo("John Doe")
    }

    @Test
    fun `getTvShowReviews should return correct userHandle`() = runTest {
        val seriesId = 4
        coEvery { tvShowRepository.getTvShowReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvShowUseCase.getTvShowReviews(seriesId, 1)

        assertThat(result[0].userHandle).isEqualTo("@johnny")
    }

    @Test
    fun `getTvShowReviews should return correct avatarUrl`() = runTest {
        val seriesId = 4
        coEvery { tvShowRepository.getTvShowReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvShowUseCase.getTvShowReviews(seriesId, 1)

        assertThat(result[0].avatarUrl).isEqualTo("https://avatar.com/john.jpg")
    }

    @Test
    fun `getTvShowReviews should return correct rating`() = runTest {
        val seriesId = 4
        coEvery { tvShowRepository.getTvShowReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvShowUseCase.getTvShowReviews(seriesId, 1)

        assertThat(result[0].rating).isEqualTo(4.5f)
    }

    @Test
    fun `getTvShowReviews should return correct content`() = runTest {
        val seriesId = 4
        coEvery { tvShowRepository.getTvShowReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvShowUseCase.getTvShowReviews(seriesId, 1)

        assertThat(result[0].content).isEqualTo("Amazing series, highly recommended!")
    }

    @Test
    fun `getTvShowReviews should return correct createdDate`() = runTest {
        val seriesId = 4
        coEvery { tvShowRepository.getTvShowReviews(seriesId, 1) } returns listOf(dummyReview)

        val result = manageTvShowUseCase.getTvShowReviews(seriesId, 1)

        assertThat(result[0].createdDate).isEqualTo(LocalDate(2023, 5, 20))
    }
    @Test
    fun `getRatedTvShows should return rated tv series when available`() = runTest {
        val accountId = 123L
        val sessionId = "session_abc"
        val expected = listOf(mockk<TvShow>(), mockk<TvShow>())
        coEvery { tvShowRepository.getRatedTvShows(accountId, sessionId) } returns expected

        val result = manageTvShowUseCase.getRatedTvShows(accountId, sessionId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getUserRatedTvSeries should return empty list when none rated`() = runTest {
        val accountId = 123L
        val sessionId = "session_abc"
        coEvery { tvShowRepository.getRatedTvShows(accountId, sessionId) } returns emptyList()

        val result = manageTvShowUseCase.getRatedTvShows(accountId, sessionId)

        assertThat(result).isEmpty()
    }


    @Test
    fun `deleteTvShowRate should return true when deletion is successful`() = runTest {
        val seriesId = 10
        coEvery { tvShowRepository.deleteTvShowRate(seriesId) } returns true

        val result = manageTvShowUseCase.deleteTvShowRate(seriesId)

        assertThat(result).isTrue()
    }

    @Test
    fun `deleteTvShowRate should return false when deletion fails`() = runTest {
        val seriesId = 11
        coEvery { tvShowRepository.deleteTvShowRate(seriesId) } returns false

        val result = manageTvShowUseCase.deleteTvShowRate(seriesId)

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
        private val dummyTvShow = TvShow(
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