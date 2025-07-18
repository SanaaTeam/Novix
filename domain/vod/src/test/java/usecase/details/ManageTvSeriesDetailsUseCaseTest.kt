package usecase.details

import com.google.common.truth.Truth.assertThat
import details.repository.TvSeriesRepository
import details.usecase.ManageTvSeriesDetailsUseCase
import entity.Actor
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries
import exceptions.NotFoundException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ManageTvSeriesDetailsUseCaseTest {

    private val tvSeriesRepository: TvSeriesRepository = mockk(relaxed = true)
    private lateinit var manageTvSeriesDetailsUseCase: ManageTvSeriesDetailsUseCase

    @BeforeEach
    fun setUp() {
        manageTvSeriesDetailsUseCase = ManageTvSeriesDetailsUseCase(tvSeriesRepository)
    }

    @Test
    fun `getTvSeriesByGenre should return list when available`() = runTest {
        val genre = Genre.WAR_AND_POLITICS
        coEvery { tvSeriesRepository.getTvSeriesByGenre(genre) } returns listOf(dummyTvSeries)
        val result = manageTvSeriesDetailsUseCase.getTvSeriesByGenre(genre)
        assertThat(result).isEqualTo(listOf(dummyTvSeries))
    }

    @Test
    fun `getTvSeriesByGenre should return empty list when none available`() = runTest {
        val genre = Genre.WAR_AND_POLITICS
        coEvery { tvSeriesRepository.getTvSeriesByGenre(genre) } returns emptyList()
        val result = manageTvSeriesDetailsUseCase.getTvSeriesByGenre(genre)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvSeriesByGenre should throw when repository fails`() = runTest {
        val genre = Genre.WAR_AND_POLITICS
        coEvery { tvSeriesRepository.getTvSeriesByGenre(genre) } throws RetrievingDataFailureException(
            "Error"
        )
        assertThrows<RetrievingDataFailureException> {
            manageTvSeriesDetailsUseCase.getTvSeriesByGenre(genre)
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
    fun `getTvSeriesDetails should throw NotFoundException when not found`() = runTest {
        val seriesId = 1
        coEvery { tvSeriesRepository.getTvSeriesDetails(seriesId) } throws NotFoundException("Not found")
        assertThrows<NotFoundException> {
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
        coEvery { tvSeriesRepository.getTvSeriesCast(seriesId) } throws NotFoundException("Error")
        assertThrows<NotFoundException> {
            manageTvSeriesDetailsUseCase.getTvSeriesCast(seriesId)
        }
    }

    @Test
    fun `getTvSeriesImages should return images when available`() = runTest {
        val seriesId = 3
        val expected = listOf("img1.jpg", "img2.jpg")
        coEvery { tvSeriesRepository.getTvSeriesImages(seriesId, any()) } returns expected
        val result = manageTvSeriesDetailsUseCase.getTvSeriesImages(seriesId)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvSeriesImages should return empty list when none available`() = runTest {
        val seriesId = 3
        coEvery { tvSeriesRepository.getTvSeriesImages(seriesId, 5) } returns emptyList()
        val result = manageTvSeriesDetailsUseCase.getTvSeriesImages(seriesId)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvSeriesImages should throw when repository fails`() = runTest {
        val seriesId = 3
        coEvery {
            tvSeriesRepository.getTvSeriesImages(
                seriesId,
                any()
            )
        } throws NotFoundException("Not found")
        assertThrows<NotFoundException> {
            manageTvSeriesDetailsUseCase.getTvSeriesImages(seriesId)
        }
    }

    @Test
    fun `getTvSeriesReviews should return reviews when available`() = runTest {
        val seriesId = 4
        val expected = listOf(mockk<Review>(), mockk<Review>())
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId) } returns expected
        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId)
        coVerify { tvSeriesRepository.getTvSeriesReviews(seriesId) }
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getTvSeriesReviews should return empty list when none available`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId) } returns emptyList()
        val result = manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvSeriesReviews should throw when repository fails`() = runTest {
        val seriesId = 4
        coEvery { tvSeriesRepository.getTvSeriesReviews(seriesId) } throws NotFoundException("Error")
        assertThrows<NotFoundException> {
            manageTvSeriesDetailsUseCase.getTvSeriesReviews(seriesId)
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

    companion object {
        private val dummyTvSeries = TvSeries(
            id = 1,
            title = "The Walking Dead",
            overview = "",
            releaseDate = LocalDate(2021, 9, 10),
            genres = listOf(Genre.WAR_AND_POLITICS),
            imdbRating = 1.2f,
            posterImageUrl = "Image",
            seasonsCount = 5
        )
    }
}
