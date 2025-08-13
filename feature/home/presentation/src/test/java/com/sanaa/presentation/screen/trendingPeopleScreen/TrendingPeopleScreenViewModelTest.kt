package com.sanaa.presentation.screen.trendingPeopleScreen

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.state.toState
import entity.Actor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import service.VodStringProvider
import usecase.ManageActorUseCase
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class TrendingPeopleScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TrendingPeopleScreenViewModel
    private lateinit var manageActorUseCase: ManageActorUseCase
    private val stringProvider: VodStringProvider = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        manageActorUseCase = mockk(relaxed = true)
    }

    @Test
    fun `init should fetch actors and update state on creation`() = runTest {
        coEvery { manageActorUseCase.getTrendingActors(any()) } returns actors

        viewModel = TrendingPeopleScreenViewModel(manageActorUseCase, stringProvider,  testDispatcher)
        testDispatcher.scheduler.advanceUntilIdle()
        val pagingData = viewModel.state.value.people
        val items = pagingData.asSnapshot()

        assertThat(items.take(actors.size)).isEqualTo(actors.map { it.toState() })
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {
        viewModel = TrendingPeopleScreenViewModel(manageActorUseCase, stringProvider,  testDispatcher)

        viewModel.onBackClick()

        val effect = viewModel.effect.first()
        assertEquals(TrendingPeopleScreenEffect.NavigateBack, effect)
    }

    @Test
    fun `onActorClick should emit NavigateToActorDetails effect with correct id`() = runTest {
        val actorId = 42
        viewModel = TrendingPeopleScreenViewModel(manageActorUseCase, stringProvider,  testDispatcher)

        viewModel.effect.test {
            viewModel.onActorClick(actorId)
            val item = awaitItem()
            assertThat(item).isEqualTo(TrendingPeopleScreenEffect.NavigateToActorDetails(actorId))
        }
    }

    companion object {
        val actors = listOf(
            Actor(
                id = 1,
                name = "Actor One",
                imageUrl = "",
                region = "",
                lastShow = "",
                gender = Actor.Gender.MALE,
                department = "",
                character = "",
                birthDate = LocalDate(2001, 8, 12),
                deathDate = LocalDate(1, 1, 1),
                placeOfBirth = "",
                biography = "",
            ),
            Actor(
                id = 2,
                name = "Actor two",
                imageUrl = "",
                region = "",
                lastShow = "",
                gender = Actor.Gender.FEMALE,
                department = "",
                character = "",
                birthDate = LocalDate(2001, 8, 12),
                deathDate = LocalDate(1, 1, 1),
                placeOfBirth = "",
                biography = "",
            ),
        )
    }
}