package com.sanaa.presentation.myAccount

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.myAccount.MyAccountScreenEffect
import com.sanaa.presentation.screen.myAccount.MyAccountScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class MyAccountScreenViewModelTest {
    private lateinit var viewModel: MyAccountScreenViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MyAccountScreenViewModel(testDispatcher)
    }

    @Test
    fun `should emit NavigateToMyRating effect when navigateToMyRating is called`() =
        runTest(testDispatcher) {
            viewModel.effect.test {
                viewModel.navigateToMyRating()
                assertThat(awaitItem()).isEqualTo(MyAccountScreenEffect.NavigateToMyRating)
            }
        }

    @Test
    fun `should emit NavigateToContentRestrictionSetting effect when navigateContentRestrictionSetting is called`() =
        runTest(testDispatcher) {
            viewModel.effect.test {
                viewModel.navigateContentRestrictionSetting()
                assertThat(awaitItem()).isEqualTo(MyAccountScreenEffect.NavigateToContentRestrictionSetting)
            }
        }

    @Test
    fun `should emit NavigateToChangePasswordSetting effect when navigateChangePasswordSetting is called`() =
        runTest(testDispatcher) {
            viewModel.effect.test {
                viewModel.navigateChangePasswordSetting()
                assertThat(awaitItem()).isEqualTo(MyAccountScreenEffect.NavigateToChangePasswordSetting)
            }
        }

    @Test
    fun `should emit NavigateToLanguageSetting effect when navigateLanguageSetting is called`() =
        runTest(testDispatcher) {
            viewModel.effect.test {
                viewModel.navigateLanguageSetting()
                assertThat(awaitItem()).isEqualTo(MyAccountScreenEffect.NavigateToLanguageSetting)
            }
        }

    @Test
    fun `should emit NavigateToWatchingHistory effect when navigateToWatchingHistory is called`() =
        runTest(testDispatcher) {
            viewModel.effect.test {
                viewModel.navigateToWatchingHistory()
                assertThat(awaitItem()).isEqualTo(MyAccountScreenEffect.NavigateToWatchingHistory)
            }
        }
}