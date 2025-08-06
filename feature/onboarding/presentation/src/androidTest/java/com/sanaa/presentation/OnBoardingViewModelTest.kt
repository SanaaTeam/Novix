//package com.sanaa.presentation
//
//import com.sanaa.presentation.screen.OnBoardingViewModel
//import kotlinx.coroutines.Dispatchers
//import org.junit.Test
//
//
//class OnboardingViewModelTest {
//
//    private lateinit var viewModel: OnBoardingViewModel
//
//    @BeforeEach
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//        viewModel = MyViewModel(
//            someUseCase = someUseCaseMock,
//            dispatcher = testDispatcher
//        )
//    }
//
//
//    @Test
//    fun `initial state has correct page list and index`() = runTest(testDispatcher) {
//        val state = viewModel.state.value
//        assertEquals(3, state.pageList.size)
//        assertEquals(0, state.currentPageIndex)
//        assertFalse(state.isSkipable)
//    }
//
//    @Test
//    fun `onNextPageClick moves to next page`() = runTest(testDispatcher) {
//        viewModel.onNextPageClick()
//        assertEquals(1, viewModel.state.value.currentPageIndex)
//
//        viewModel.onNextPageClick()
//        assertEquals(2, viewModel.state.value.currentPageIndex)
//    }
//
//    @Test
//    fun `onNextPageClick on last page triggers skip`() = runTest(testDispatcher) {
//        // Move to last page
//        viewModel.setCurrentPage(2)
//        viewModel.onNextPageClick()
//
//        val state = viewModel.state.value
//        assertTrue(state.isSkipable)
//    }
//
//    @Test
//    fun `onBackClick moves to previous page`() = runTest(testDispatcher) {
//        viewModel.setCurrentPage(2)
//        viewModel.onBackClick()
//        assertEquals(1, viewModel.state.value.currentPageIndex)
//
//        viewModel.onBackClick()
//        assertEquals(0, viewModel.state.value.currentPageIndex)
//    }
//
//    @Test
//    fun `onBackClick on first page does nothing`() = runTest(testDispatcher) {
//        viewModel.setCurrentPage(0)
//        viewModel.onBackClick()
//        assertEquals(0, viewModel.state.value.currentPageIndex)
//    }
//
//    @Test
//    fun `onSkipClick sets isSkipable to true`() = runTest(testDispatcher) {
//        viewModel.onSkipClick()
//        assertTrue(viewModel.state.value.isSkipable)
//    }
//
//    @Test
//    fun `setCurrentPage updates currentPageIndex`() = runTest(testDispatcher) {
//        viewModel.setCurrentPage(1)
//        assertEquals(1, viewModel.state.value.currentPageIndex)
//    }
//}
