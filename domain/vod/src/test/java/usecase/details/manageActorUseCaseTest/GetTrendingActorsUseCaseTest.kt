package usecase.details.manageActorUseCaseTest

import usecase.manageActorUseCase.GetTrendingActorsUseCase
import com.google.common.truth.Truth.assertThat
import entity.Actor
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.ActorRepository


class GetTrendingActorsUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getTrendingActorsUseCase: GetTrendingActorsUseCase

    @BeforeEach
    fun setUp() {
        getTrendingActorsUseCase = GetTrendingActorsUseCase(actorRepository)
    }

    @Test
    fun `getTrendingActors should call repository and return list of actors`() = runTest {
        val trendingActors = listOf(dummyActor, dummyActor.copy(id = 2, name = "Jane Smith"))
        coEvery { actorRepository.getTrendingActors(1) } returns trendingActors
        val result = getTrendingActorsUseCase(1)
        assertThat(result).isEqualTo(trendingActors)
    }

    @Test
    fun `getTrendingActors should throw when repository fails`() = runTest {
        coEvery { actorRepository.getTrendingActors(1) } throws NovixAppException("Error fetching trending actors")
        assertThrows<NovixAppException> {
            getTrendingActorsUseCase(1)
        }
    }

    companion object {
        private val dummyActor = Actor(
            id = 1,
            imageUrl = "https://image.tmdb.org/t/p/w500/xyz.jpg",
            name = "John Doe",
            character = "Acting",
            birthDate = LocalDate(1980, 1, 1),
            deathDate = LocalDate(1, 1, 1),
            placeOfBirth = "Somewhere, USA",
            biography = "A short bio text.",
            department = "Acting"
        )
    }
}