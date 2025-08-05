package usecase

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.ContentRestriction
import repository.Language
import repository.Theme
import repository.UserPreferencesRepository


class MangeUserPreferenceUseCaseTest {

    lateinit var mangeUserPreferenceUseCase: MangeUserPreferenceUseCase
    private val userPreferenceRepository: UserPreferencesRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        mangeUserPreferenceUseCase = MangeUserPreferenceUseCase(userPreferenceRepository)
    }

    @Test
    fun `setLanguage should call userPreferenceRepository setLanguage`() = runTest {
        val language = Language.ENGLISH

        mangeUserPreferenceUseCase.setLanguage(language)

        coVerify { userPreferenceRepository.setLanguage(language) }
    }

    @Test
    fun `getLanguage should return userPreferenceRepository getLanguage`() = runTest {
        val expectedFlow: Flow<Language> = flowOf(Language.ENGLISH)

        every { userPreferenceRepository.getLanguage() } returns expectedFlow

        val result = mangeUserPreferenceUseCase.getLanguage()

        result.collect { value ->
            assert(value == Language.ENGLISH)
        }
    }

    @Test
    fun `setTheme should call userPreferenceRepository setTheme`() = runTest {
        val theme = Theme.DARK

        mangeUserPreferenceUseCase.setTheme(theme)

        coVerify { userPreferenceRepository.setTheme(theme) }
    }

    @Test
    fun `getTheme should return userPreferenceRepository getTheme`() = runTest {
        val expectedFlow: Flow<Theme> = flowOf(Theme.LIGHT)

        every { userPreferenceRepository.getTheme() } returns expectedFlow

        val result = mangeUserPreferenceUseCase.getTheme()

        result.collect { value ->
            assert(value == Theme.LIGHT)
        }
    }

    @Test
    fun `setContentRestriction should call userPreferenceRepository setContentRestriction`() = runTest {
        val contentRestriction = ContentRestriction.RESTRICTED

        mangeUserPreferenceUseCase.setContentRestriction(contentRestriction)

        coVerify { userPreferenceRepository.setContentRestriction(contentRestriction) }
    }

    @Test
    fun `getContentRestriction should return userPreferenceRepository getContentRestriction`() = runTest {
        val expectedFlow: Flow<ContentRestriction> = flowOf(ContentRestriction.UNRESTRICTED)

        every { userPreferenceRepository.getContentRestriction() } returns expectedFlow

        val result = mangeUserPreferenceUseCase.getContentRestriction()

        result.collect { value ->
            assert(value == ContentRestriction.UNRESTRICTED)
        }
    }
}