package com.sanaa.presentation.screen.playlistDetails

import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.playlist.PlayListScreenEffect
import com.sanaa.presentation.screen.playlist.PlayListScreenUiState
import com.sanaa.presentation.screen.playlist.PlayListScreenViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SavedListsStatusProvider
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.custom_list.custom_list_param.SavedList

@OptIn(ExperimentalCoroutinesApi::class)
class PlayListScreenViewModelTest {

    private lateinit var viewModel: PlayListScreenViewModel
    private val listStatusProvider: SavedListsStatusProvider = mockk(relaxed = true)
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private val fakeListsFlow = MutableStateFlow<List<SavedList>>(emptyList())

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { listStatusProvider.savedLists } returns fakeListsFlow
        coEvery { listStatusProvider.refreshLists() } returns Unit
        every { checkIfUserIsLoggedInUseCase.isLoggedIn() } returns flowOf(true)
    }

    @Test
    fun `onListAdded emits ShowSuccessToAddListSnackBar and reloads lists`() = runTest {
        viewModel = PlayListScreenViewModel(
            checkUserLogin = checkIfUserIsLoggedInUseCase,
            listsStatusProvider = listStatusProvider,
            dispatcher = testDispatcher
        )

        val effectEvents = mutableListOf<PlayListScreenEffect>()
        val effectJob = launch {
            viewModel.effect.collect { effectEvents.add(it) }
        }

        val stateEvents = mutableListOf<PlayListScreenUiState>()
        val stateJob = launch {
            viewModel.state.collect { stateEvents.add(it) }
        }

        advanceUntilIdle()

        val newLists = listOf(fakeDomainList(1))
        fakeListsFlow.value = newLists

        viewModel.onListAdded()

        advanceUntilIdle()

        assertThat(effectEvents).containsExactly(PlayListScreenEffect.ShowSuccessToAddListSnackBar)

        val latestState = stateEvents.last()
        assertThat(latestState.lists).hasSize(1)
        assertThat(latestState.lists.first().title).isEqualTo("List 1")

        effectJob.cancel()
        stateJob.cancel()
    }

    @Test
    fun `onListDeletedSuccessfully emits ShowSuccessToDeleteListSnackBar and reloads lists`() =
        runTest {
            viewModel = PlayListScreenViewModel(
                checkUserLogin = checkIfUserIsLoggedInUseCase,
                listsStatusProvider = listStatusProvider,
                dispatcher = testDispatcher
            )

            val effectEvents = mutableListOf<PlayListScreenEffect>()
            val effectJob = launch {
                viewModel.effect.collect { effectEvents.add(it) }
            }

            val stateEvents = mutableListOf<PlayListScreenUiState>()
            val stateJob = launch {
                viewModel.state.collect { stateEvents.add(it) }
            }

            advanceUntilIdle() // initial emissions

            val newLists = listOf(fakeDomainList(2, "Deleted List"))
            fakeListsFlow.value = newLists

            viewModel.onListDeletedSuccessfully()

            advanceUntilIdle()

            assertThat(effectEvents).containsExactly(PlayListScreenEffect.ShowSuccessToDeleteListSnackBar)

            val latestState = stateEvents.last()
            assertThat(latestState.lists).hasSize(1)
            assertThat(latestState.lists.first().title).isEqualTo("Deleted List")

            effectJob.cancel()
            stateJob.cancel()
        }

    private fun fakeDomainList(
        id: Int = 1,
        title: String = "List $id",
        itemCount: Int = 3
    ): SavedList = SavedList(id = id, title = title, itemCount = itemCount)
}