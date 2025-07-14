package usecase.details.actor

import com.google.common.truth.Truth.assertThat
import details.repository.ActorRepository
import details.usecase.actor.GetProfileImagesUseCase
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetProfileImagesUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getProfileImagesUseCase: GetProfileImagesUseCase

    @BeforeEach
    fun setUp() {
        getProfileImagesUseCase = GetProfileImagesUseCase(actorRepository)
    }

    @Test
    fun `execute() should call getProfileImages() from ActorRepository`() = runTest {
        // Given
        val actorId = 11

        // When
        getProfileImagesUseCase.execute(actorId)

        // Then
        coVerify { actorRepository.getProfileImages(actorId) }
    }

    @Test
    fun `execute() should return profile list when repository succeeds`() = runTest {
        // Given
        val actorId = 5
        val expected = dummyProfiles
        coEvery { actorRepository.getProfileImages(actorId) } returns expected

        // When
        val result = getProfileImagesUseCase.execute(actorId)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `execute() should throw RetrievingDataFailureException when repository fails`() = runTest {
        // Given
        val actorId = 999
        coEvery {
            actorRepository.getProfileImages(actorId)
        } throws RetrievingDataFailureException("Timeout")

        // When / Then
        assertThrows<RetrievingDataFailureException> {
            getProfileImagesUseCase.execute(actorId)
        }
    }

    companion object {
        private val dummyProfiles = listOf(
            "https://image.tmdb.org/t/p/w500/profile1.jpg",
            "https://image.tmdb.org/t/p/w500/profile2.jpg",
            "https://image.tmdb.org/t/p/w500/profile3.jpg"
        )
    }
}
