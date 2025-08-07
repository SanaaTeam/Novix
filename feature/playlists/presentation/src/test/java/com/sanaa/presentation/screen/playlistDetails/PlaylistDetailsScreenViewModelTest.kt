//package com.sanaa.presentation.screen.playlistDetails
//
//import androidx.lifecycle.SavedStateHandle
//import app.cash.turbine.test
//import com.google.common.truth.Truth.assertThat
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import usecase.custom_list.ManageSavedListItemsUseCase
//import usecase.custom_list.ManageSavedListsUseCase
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class PlaylistDetailsScreenViewModelTest {
//
//    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase = mockk(relaxed = true)
//    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)
//    private lateinit var viewModel: PlaylistDetailsScreenViewModel
//    private val testDispatcher = StandardTestDispatcher()
//    private lateinit var savedStateHandle: SavedStateHandle
//
//    @BeforeEach
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//        savedStateHandle = SavedStateHandle(
//            mapOf(
//                "listId" to 1,
//                "title" to "My Playlist"
//            )
//        )
//        coEvery {
//            manageSavedListItemsUseCase.getAllItemsInSavedList(any(), any())
//        } returns emptyList()
//    }
//
//    @Test
//    fun `onBackClick emits NavigateBack`() = runTest {
//        initViewModel()
//        viewModel.onBackClick()
//        viewModel.effect.test {
//            assertThat(awaitItem()).isEqualTo(PlaylistDetailsScreenEffect.NavigateBack)
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `onDeleteListClicked sets showBottomSheet true`() = runTest {
//        initViewModel()
//        viewModel.onDeleteListClicked()
//        assertThat(viewModel.state.value.showBottomSheet).isTrue()
//    }
//
//    @Test
//    fun `onDismissBottomSheet sets showBottomSheet false`() = runTest {
//        initViewModel()
//        viewModel.updateState { it.copy(showBottomSheet = true) }
//        viewModel.onDismissBottomSheet()
//        assertThat(viewModel.state.value.showBottomSheet).isFalse()
//    }
//
//    @Test
//    fun `onListDeletedSuccessfully emits NavigateBack`() = runTest {
//        initViewModel()
//        viewModel.onListDeletedSuccessfully()
//        viewModel.effect.test {
//            assertThat(awaitItem()).isEqualTo(PlaylistDetailsScreenEffect.NavigateBack)
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//
//
//    private fun initViewModel() {
//        viewModel = PlaylistDetailsScreenViewModel(
//            savedStateHandle = savedStateHandle,
//            manageSavedListItemsUseCase = manageSavedListItemsUseCase,
//            dispatcher = testDispatcher
//        )
//    }
//}
