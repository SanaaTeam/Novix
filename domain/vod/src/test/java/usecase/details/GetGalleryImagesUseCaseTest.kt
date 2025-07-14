package usecase.details

import com.google.common.truth.Truth.assertThat
import details.repository.ActorRepository
import details.usecase.actor.GetGalleryImagesUseCase
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetGalleryImagesUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getGalleryImagesUseCase: GetGalleryImagesUseCase

    @BeforeEach
    fun setUp() {
        getGalleryImagesUseCase = GetGalleryImagesUseCase(actorRepository)
    }

    @Test
    fun `execute() should call getGalleryImages() from ActorRepository`() = runTest {
        // Given
        val actorId = 7

        // When
        getGalleryImagesUseCase.execute(actorId)

        // Then
        coVerify { actorRepository.getGalleryImages(actorId) }
    }

    @Test
    fun `execute() should return gallery list when repository succeeds`() = runTest {
        // Given
        val actorId = 3
        val expected = dummyGallery
        coEvery { actorRepository.getGalleryImages(actorId) } returns expected

        // When
        val result = getGalleryImagesUseCase.execute(actorId)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `execute() should throw RetrievingDataFailureException when repository fails`() = runTest {
        // Given
        val actorId = 404
        coEvery {
            actorRepository.getGalleryImages(actorId)
        } throws RetrievingDataFailureException("Backend error")

        // When / Then
        assertThrows<RetrievingDataFailureException> {
            getGalleryImagesUseCase.execute(actorId)
        }
    }

    companion object {
        private val dummyGallery = listOf(
            "https://image.tmdb.org/t/p/w780/backdrop1.jpg",
            "https://image.tmdb.org/t/p/w780/backdrop2.jpg",
            "https://image.tmdb.org/t/p/w780/backdrop3.jpg"
        )
    }
}
