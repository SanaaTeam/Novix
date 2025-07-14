package usecase

import com.google.common.truth.Truth.assertThat
import details.repository.ActorRepository
import details.usecase.actor.GetActorDetailsUseCase
import entity.Actor
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetActorDetailsUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getActorDetailsUseCase: GetActorDetailsUseCase

    @BeforeEach
    fun setUp() {
        getActorDetailsUseCase = GetActorDetailsUseCase(actorRepository)
    }

    @Test
    fun `execute() should call getActorDetails() from ActorRepository`() = runTest {
        // Given
        val actorId = 42

        // When
        getActorDetailsUseCase.execute(actorId)

        // Then
        coVerify { actorRepository.getActorDetails(actorId) }
    }

    @Test
    fun `execute() should return actor when repository succeeds`() = runTest {
        // Given
        val actorId = 1
        val expected = dummyActor
        coEvery { actorRepository.getActorDetails(actorId) } returns expected

        // When
        val result = getActorDetailsUseCase.execute(actorId)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `execute() should throw RetrievingDataFailureException when repository fails`() = runTest {
        // Given
        val actorId = 99
        coEvery {
            actorRepository.getActorDetails(actorId)
        } throws RetrievingDataFailureException("Network error")

        // When / Then
        assertThrows<RetrievingDataFailureException> {
            getActorDetailsUseCase.execute(actorId)
        }
    }

    companion object {
        private val dummyActor = Actor(
            id = 1,
            imageUrl = "https://image.tmdb.org/t/p/w500/xyz.jpg",
            name = "John Doe",
            region = "US",
            lastShow = "Some Movie (2024)",
            gender = Actor.Gender.MALE,
            character = "Acting",
            birthDate = LocalDate(1980, 1, 1),
            deathDate = null,
            placeOfBirth = "Somewhere, USA",
            biography = "A short bio text."
        )
    }
}
