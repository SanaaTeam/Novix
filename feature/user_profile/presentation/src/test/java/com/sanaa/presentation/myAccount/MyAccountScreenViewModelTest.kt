package com.sanaa.presentation.myAccount

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect
import com.sanaa.presentation.screen.myAccount.MyAccountScreenUiState.ContentRestrictionUiState
import com.sanaa.presentation.screen.myAccount.MyAccountScreenUiState.ThemeUiState
import com.sanaa.presentation.screen.myAccount.MyAccountScreenViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.ContentRestriction
import repository.Language
import repository.Theme
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.LogOutUseCase
import usecase.MangeUserPreferenceUseCase
import usecase.custom_list.ManageSavedListsUseCase

@ExperimentalCoroutinesApi
class MyAccountScreenViewModelTest {

    private lateinit var viewModel: MyAccountScreenViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val mangeUserPreference: MangeUserPreferenceUseCase = mockk(relaxed = true)
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase = mockk(relaxed = true)
    private val manageSavedListsUseCase:ManageSavedListsUseCase = mockk(relaxed = true)
    private val logOutUseCase: LogOutUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { mangeUserPreference.getLanguage() } returns flowOf(Language.ENGLISH)
        coEvery { mangeUserPreference.getTheme() } returns flowOf(Theme.DARK)
        coEvery { mangeUserPreference.getContentRestriction() } returns flowOf(ContentRestriction.RESTRICTED)

        viewModel = MyAccountScreenViewModel(
            mangeUserPreference,
            checkIfUserIsLoggedInUseCase,
            getLoggedInUserUseCase,
            logOutUseCase,
            manageSavedListsUseCase,
            testDispatcher,
        )
    }

    @Test
    fun `should emit NavigateToMyRating effect when onClickMyTopRating is called`() = runTest {
        viewModel.effect.test {
            viewModel.onClickMyTopRating()
            assertThat(awaitItem()).isEqualTo(MyAccountScreenEffect.NavigateToMyRating)
        }
    }

    @Test
    fun `should emit NavigateToWatchingHistory effect when onClickWatchingHistory is called`() =
        runTest {
            viewModel.effect.test {
                viewModel.onClickWatchingHistory()
                assertThat(awaitItem()).isEqualTo(MyAccountScreenEffect.NavigateToWatchingHistory)
            }
        }

    @Test
    fun `should emit NavigateToChangePasswordSetting effect when onClickChangePassword is called`() =
        runTest {
            viewModel.effect.test {
                viewModel.onClickChangePassword()
                assertThat(awaitItem()).isEqualTo(MyAccountScreenEffect.NavigateToChangePasswordSetting)
            }
        }

    @Test
    fun `should update state to showChangeLanguageBottomSheet true when onClickLanguageSetting is called`() =
        runTest {
            viewModel.onClickLanguageSetting()
            assertThat(viewModel.state.value.showChangeLanguageBottomSheet).isTrue()
        }

    @Test
    fun `should update state to showContentRestrictionBottomSheet true when onClickContentRestriction is called`() =
        runTest {
            viewModel.onClickContentRestriction()
            assertThat(viewModel.state.value.showContentRestrictionBottomSheet).isTrue()
        }

    @Test
    fun `should call mangeUserPreference setLanguage and emit UpdateAppLanguage when onSaveLanguageClick is called`() =
        runTest {
            val selectedLanguage = Language.ENGLISH

            coEvery { mangeUserPreference.getLanguage() } returns flowOf(Language.ARABIC)
            coEvery { mangeUserPreference.setLanguage(any()) } returns Unit

            viewModel.updateState { copy(savedLanguage = Language.ARABIC.code, selectedLanguage = selectedLanguage.code) }

            viewModel.effect.test {
                viewModel.onSaveLanguageClick()
                coVerify { mangeUserPreference.setLanguage(selectedLanguage) }
                val effect = awaitItem()
                assertThat(effect).isInstanceOf(MyAccountScreenEffect.UpdateAppLanguage::class.java)
                assertThat((effect as MyAccountScreenEffect.UpdateAppLanguage).language)
                    .isEqualTo(selectedLanguage.code)
            }
        }

    @Test
    fun `should call mangeUserPreference setTheme and emit UpdateAppTheme when onSaveThemeClick is called`() =
        runTest {
            val selectedTheme = Theme.DARK

            coEvery { mangeUserPreference.getTheme() } returns flowOf(Theme.LIGHT)
            coEvery { mangeUserPreference.setTheme(any()) } returns Unit

            viewModel.updateState {
                copy(
                    savedTheme = ThemeUiState.LIGHT,
                    selectedTheme = selectedTheme.toUiState()
                )
            }

            viewModel.effect.test {
                viewModel.onSaveThemeClick()
                coVerify { mangeUserPreference.setTheme(selectedTheme) }
                val effect = awaitItem()
                assertThat(effect).isInstanceOf(MyAccountScreenEffect.UpdateAppTheme::class.java)
                assertThat((effect as MyAccountScreenEffect.UpdateAppTheme).isDarkMode).isTrue()
            }
        }

    @Test
    fun `should call mangeUserPreference setContentRestriction when onSaveContentRestrictionClick is called`() =
        runTest {
            val restriction = ContentRestriction.RESTRICTED

            coEvery { mangeUserPreference.getContentRestriction() } returns flowOf(ContentRestriction.UNRESTRICTED)
            coEvery { mangeUserPreference.setContentRestriction(any()) } returns Unit

            viewModel.updateState {
                copy(
                    savedContentRestriction = ContentRestrictionUiState.UNRESTRICTED,
                    selectedContentRestriction = ContentRestrictionUiState.valueOf(restriction.name)
                )
            }

            viewModel.onSaveContentRestrictionClick()
            coVerify { mangeUserPreference.setContentRestriction(restriction) }
        }

    private fun Theme.toUiState() =
        ThemeUiState.valueOf(this.name)
}