package usecase

import com.google.common.truth.Truth.assertThat
import entity.User
import exceptions.NoLoggedInUserException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.AuthenticationRepository

class GetLoggedInUserUseCaseTest {
    private val repository: AuthenticationRepository = mockk()
    private lateinit var useCase: GetLoggedInUserUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetLoggedInUserUseCase(repository)
    }

    @Test
    fun `Should return User when repository returns User`() = runTest {
        val expectedUser = User(
            id = 1,
            name = "Novix User",
            username = "novix",
            profileImageUrl = "Url"
        )
        coEvery { repository.getLoggedUser() } returns flowOf(expectedUser)

        val result = useCase.getLoggedInUser().first()

        assertThat(result.id).isEqualTo(expectedUser.id)
    }

    @Test
    fun `Should throw NoLoggedInUserException when repository throws`() = runTest {
        coEvery { repository.getLoggedUser() } throws NoLoggedInUserException()

        assertThrows<NoLoggedInUserException> { useCase.getLoggedInUser() }
    }
}