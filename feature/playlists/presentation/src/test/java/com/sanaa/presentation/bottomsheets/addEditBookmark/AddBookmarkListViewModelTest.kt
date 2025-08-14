package com.sanaa.presentation.bottomsheets.addEditBookmark

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList

@OptIn(ExperimentalCoroutinesApi::class)
class AddBookmarkListViewModelTest {

    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)

    private lateinit var viewModel: AddBookmarkListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init calls refreshLists on the provider`() = runTest {
        initViewModel()
        advanceUntilIdle()
    }


    @Test
    fun `onAddClicked on success creates list, updates provider, and emits success effect`() =
        runTest {
            val listTitle = "My New List"
            val mediaId = 123
            val fakeSavedList = SavedList(id = 1, title = listTitle, itemCount = 1)

            coEvery { manageSavedListsUseCase.createSavedList(listTitle) } returns fakeSavedList
            initViewModel()
            viewModel.onListTitleChanged(listTitle)

            viewModel.effect.test {
                viewModel.onAddClicked(mediaId)
                advanceUntilIdle()

                assertThat(awaitItem()).isEqualTo(AddBookmarksEffect.AddSuccess)

                coVerify(exactly = 1) { manageSavedListsUseCase.createSavedList(listTitle) }

                val state = viewModel.state.value
                assertThat(state.listTitle).isEmpty()
                assertThat(state.isLoading).isFalse()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onAddClicked on failure updates state with error and emits failure effect`() = runTest {
        val listTitle = "Failing List"
        val error = RuntimeException("Database error")

        coEvery { manageSavedListsUseCase.createSavedList(listTitle) } throws error
        initViewModel()
        viewModel.onListTitleChanged(listTitle)

        viewModel.effect.test {
            viewModel.onAddClicked(456)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(AddBookmarksEffect.AddFailure)

            val state = viewModel.state.value
            assertThat(state.isLoading).isFalse()
            assertThat(state.errorMessage).isEqualTo("Failed to create list. Please try again.")

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun initViewModel() {
        viewModel = AddBookmarkListViewModel(
            manageSavedListsUseCase = manageSavedListsUseCase,
            dispatcher = testDispatcher
        )
    }
}