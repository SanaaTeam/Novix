package com.sanaa.presentation.screen.playlistDetails

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.playlist.PlayListScreenEffect
import com.sanaa.presentation.screen.playlist.PlayListScreenViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.custom_list.custom_list_param.SavedList

class PlayListScreenViewModelTest {

    private lateinit var viewModel: PlayListScreenViewModel
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `onFabBottomSheetClicked sets showAddBottomSheet true`() = runTest {
        initViewModel()
        viewModel.onFabBottomSheetClicked()
        assertThat(viewModel.state.value.showAddBottomSheet).isTrue()
    }

    @Test
    fun `onDismissAddBottomSheet sets showAddBottomSheet false`() = runTest {
        initViewModel()
        viewModel.updateState { copy(showAddBottomSheet = true) }
        viewModel.onDismissAddBottomSheet()
        assertThat(viewModel.state.value.showAddBottomSheet).isFalse()
    }

    @Test
    fun `onButtonLoginClicked emits NavigateToLogin`() = runTest {
        initViewModel()
        viewModel.effect.test {
            viewModel.onButtonLoginClicked()
            assertThat(awaitItem()).isEqualTo(PlayListScreenEffect.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onItemListClicked emits NavigateToSavedDetails`() = runTest {
        initViewModel()
        viewModel.effect.test {
            viewModel.onItemListClicked(5, "Workout Playlist")
            assertThat(awaitItem()).isEqualTo(
                PlayListScreenEffect.NavigateToSavedDetails(5, "Workout Playlist")
            )
            cancelAndIgnoreRemainingEvents()
        }
    }



    private fun initViewModel() {
        viewModel = PlayListScreenViewModel(
            checkUserLogin = checkIfUserIsLoggedInUseCase
        )
    }
}