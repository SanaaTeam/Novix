package usecase.tv_series

import details.repository.TvSeriesRepository
import details.usecase.tv_series.GetTvSeriesCastUseCase
import entity.Actor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvSeriesCastUseCaseTest {
    private lateinit var getTvSeriesCastUseCase: GetTvSeriesCastUseCase
    private val repository: TvSeriesRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getTvSeriesCastUseCase = GetTvSeriesCastUseCase(repository)
    }

    @Test
    fun `execute() should return a list of tv series cast when id is provided`() = runTest {
        // Given
        coEvery { repository.getTvSeriesCast(any()) } returns listOf(dummyActor)
        // When
        val result = getTvSeriesCastUseCase.execute(1)
        // Then
        assert(result.isNotEmpty())
    }

    @Test
    fun `execute() should return an empty list when no tv series cast are found for the given id`() =
        runTest {
            // Given
            coEvery { repository.getTvSeriesCast(any()) } returns emptyList()
            // When
            val result = getTvSeriesCastUseCase.execute(1)
            // Then
            assert(result.isEmpty())
        }


    val dummyActor = Actor(
        id = 1,
        imageUrl = "Image",
        name = "Tom Holland",
        age = 21,
        region = "Iraq",
        lastShow = "Spider man",
        gender = Actor.Gender.MALE,
    )
}