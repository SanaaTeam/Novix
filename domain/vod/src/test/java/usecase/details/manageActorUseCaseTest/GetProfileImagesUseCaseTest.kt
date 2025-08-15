package usecase.details.manageActorUseCaseTest

import usecase.manageActorUseCase.GetActorProfileImagesUseCase
import com.google.common.truth.Truth.assertThat
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.ActorRepository


class GetProfileImagesUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var getActorProfileImagesUseCase: GetActorProfileImagesUseCase

    @BeforeEach
    fun setUp() {
        getActorProfileImagesUseCase = GetActorProfileImagesUseCase(actorRepository)
    }

    @Test
    fun `getProfileImages should call repository and return list`() = runTest {
        val actorId = 9
        coEvery { actorRepository.getProfileImageUrls(actorId, 10) } returns dummyProfiles
        val result = getActorProfileImagesUseCase(actorId, 10)
        assertThat(result).isEqualTo(dummyProfiles)
    }

    @Test
    fun `getProfileImages should throw when repository fails`() = runTest {
        val actorId = 10
        coEvery { actorRepository.getProfileImageUrls(actorId, 10) } throws NovixAppException("Error")
        assertThrows<NovixAppException> {
            getActorProfileImagesUseCase(actorId, 10)
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