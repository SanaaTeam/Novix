package com.sanaa.presentation.screen.trendingMediaScreen.celebritiesScreen

import com.sanaa.presentation.screen.celebritiesScreen.CelebritiesScreenEffects
import com.sanaa.presentation.screen.celebritiesScreen.CelebritiesViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import usecase.ManageActorUseCase
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class CelebritiesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CelebritiesViewModel
    private var manageActorUseCase: ManageActorUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CelebritiesViewModel(manageActorUseCase)
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {
        viewModel.onBackClick()
        val effect = viewModel.effect.first()
        assertEquals(CelebritiesScreenEffects.NavigateBack, effect)
    }

    @Test
    fun `onActorClick should emit NavigateToActorDetails effect with correct id`() = runTest {
        val actorId = 42
        viewModel.onActorClick(actorId)
        val effect = viewModel.effect.first()
        assertEquals(CelebritiesScreenEffects.NavigateToActorDetails(actorId), effect)
    }
}