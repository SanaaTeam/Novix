package com.sanaa.presentation.screen.playlistDetails

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.playlist.PlayListScreenEffect
import com.sanaa.presentation.screen.playlist.PlayListScreenViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList

class PlayListScreenViewModelTest {

    private lateinit var viewModel: PlayListScreenViewModel
    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val testScheduler = testDispatcher.scheduler

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
        viewModel.updateState { it.copy(showAddBottomSheet = true) }
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
                PlayListScreenEffect.NavigateToSavedDetails(
                    5,
                    "Workout Playlist"
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onListAddFailed emits ShowErrorToAddListSnackBar`() = runTest {
        initViewModel()
        viewModel.effect.test {
            viewModel.onListAddFailed()
            assertThat(awaitItem()).isEqualTo(PlayListScreenEffect.ShowErrorToAddListSnackBar)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onListAdded emits ShowSuccessToAddListSnackBar and reloads lists`() = runTest {
        coEvery { manageSavedListsUseCase.getSavedLists() } returns listOf(fakeDomainList(1))
        every { checkIfUserIsLoggedInUseCase.isLoggedIn() } returns flowOf(true)

        initViewModel()
        viewModel.effect.test {
            viewModel.onListAdded()
            assertThat(awaitItem()).isEqualTo(PlayListScreenEffect.ShowSuccessToAddListSnackBar)
            cancelAndIgnoreRemainingEvents()
        }

        assertThat(viewModel.state.value.lists).hasSize(1)
    }

    @Test
    fun `onListDeletedSuccessfully emits ShowSuccessToDeleteListSnackBar and reloads lists`() = runTest {
        coEvery { manageSavedListsUseCase.getSavedLists() } returns listOf(fakeDomainList(1))
        every { checkIfUserIsLoggedInUseCase.isLoggedIn() } returns flowOf(true)

        initViewModel()
        viewModel.effect.test {
            viewModel.onListDeletedSuccessfully()

            assertThat(awaitItem()).isEqualTo(PlayListScreenEffect.ShowSuccessToDeleteListSnackBar)
            cancelAndIgnoreRemainingEvents()
        }

        assertThat(viewModel.state.value.lists).hasSize(1)
    }

    private fun initViewModel() {
        viewModel = PlayListScreenViewModel(
            checkUserLogin = checkIfUserIsLoggedInUseCase,
            manageSavedListsUseCase = manageSavedListsUseCase
        )
    }

    private fun fakeDomainList(
        id: Int = 1,
        title: String = "List $id",
        itemCount: Int = 3
    ): SavedList {
        return SavedList(
            id = id,
            title = title,
            itemCount = itemCount
        )
    }
}