package usecase.details.manageActorUseCaseTest

import usecase.manageActorUseCase.GetActorDetailsUseCase
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


class GetActorDetailsUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getActorDetailsUseCase: GetActorDetailsUseCase

    @BeforeEach
    fun setUp() {
        getActorDetailsUseCase = GetActorDetailsUseCase(actorRepository)
    }

    @Test
    fun `getActorDetails should call repository and return Actor`() = runTest {
        val actorId = 1
        coEvery { actorRepository.getActorDetails(actorId) } returns dummyActor
        val result = getActorDetailsUseCase(actorId)
        assertThat(result).isEqualTo(dummyActor)
    }

    @Test
    fun `getActorDetails should throw when repository fails`() = runTest {
        val actorId = 2
        coEvery { actorRepository.getActorDetails(actorId) } throws NovixAppException("Error")
        assertThrows<NovixAppException> {
            getActorDetailsUseCase(actorId)
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