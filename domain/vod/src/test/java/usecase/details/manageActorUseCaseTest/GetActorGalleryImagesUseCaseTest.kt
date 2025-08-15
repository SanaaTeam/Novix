package usecase.details.manageActorUseCaseTest

import usecase.manageActorUseCase.GetActorGalleryImagesUseCase
import com.google.common.truth.Truth.assertThat
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.ActorRepository


class GetActorGalleryImagesUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getActorGalleryImagesUseCase: GetActorGalleryImagesUseCase

    @BeforeEach
    fun setUp() {
        getActorGalleryImagesUseCase = GetActorGalleryImagesUseCase(actorRepository)
    }

    @Test
    fun `getGalleryImages should call repository and return list`() = runTest {
        val actorId = 7
        coEvery { actorRepository.getGalleryImageUrls(actorId) } returns dummyGallery
        val result = getActorGalleryImagesUseCase(actorId)
        assertThat(result).isEqualTo(dummyGallery)
    }

    @Test
    fun `getGalleryImages should throw when repository fails`() = runTest {
        val actorId = 8
        coEvery { actorRepository.getGalleryImageUrls(actorId) } throws NovixAppException("Error")
        assertThrows<NovixAppException> {
            getActorGalleryImagesUseCase(actorId)
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