package test


import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.novix.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.Theme
import usecase.MangeUserPreferenceUseCase

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val mangeUserPreference: MangeUserPreferenceUseCase = mockk()

    private val themeFlow = MutableSharedFlow<Theme>(replay = 1)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mangeUserPreference.getTheme() } returns themeFlow.asSharedFlow()
        viewModel = MainViewModel(mangeUserPreference)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have isReady true`() = runTest {
        viewModel.state.test {
            val firstState = awaitItem()
            assertThat(firstState.isReady).isTrue()
        }
    }

    @Test
    fun `should update isDarkTheme when theme is LIGHT`() = runTest {
        viewModel.state.test {
            skipItems(1)
            themeFlow.emit(Theme.LIGHT)
            val updatedState = awaitItem()
            assertThat(updatedState.isDarkTheme).isFalse()
        }
    }
}