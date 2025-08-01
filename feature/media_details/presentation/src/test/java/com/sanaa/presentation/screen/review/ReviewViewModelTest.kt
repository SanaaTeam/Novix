package com.sanaa.presentation.screen.review

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.model.mapper.toReviewUiModel
import entity.Review
import exceptions.NoNetworkException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var manageMovieDetails: ManageMovieUseCase
    private lateinit var manageTvSeriesDetails: ManageTvSeriesUseCase
    private lateinit var viewModel: ReviewViewModel
    private val mediaId = 101

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        manageMovieDetails = mockk(relaxed = true)
        manageTvSeriesDetails = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onBackClick should emit NavigateBack`() = runTest {
        viewModel = givenHappyViewModel(MediaTypeUiModel.MOVIE)

        viewModel.onBackClick()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ReviewScreenEffects.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun `ReviewScreenInteractionListener calls onBackClick`() {
        var backClicked = false
        val listener = object : ReviewScreenInteractionListener {
            override fun onBackClick() {
                backClicked = true
            }

            override fun onRetryClicked() {}

        }

        listener.onBackClick()
        assertThat(backClicked).isTrue()
    }
    @Test
    fun `fetchReviews should update reviews for movies`() = runTest {
        coEvery { manageMovieDetails.getReviewsByMovieId(mediaId, any()) } returns dummyReviews

        viewModel = givenHappyViewModel(MediaTypeUiModel.MOVIE)
        testDispatcher.scheduler.advanceUntilIdle()

        val pagingData = viewModel.state.value.reviews
        val items = pagingData.asSnapshot()
        assertThat(items.take(dummyReviews.size)).isEqualTo(dummyReviews.map { it.toReviewUiModel() })
    }

    @Test
    fun `fetchReviews should update reviews for tv series`() = runTest {
        coEvery { manageTvSeriesDetails.getTvSeriesReviews(mediaId, any()) } returns dummyReviews

        viewModel = givenHappyViewModel(MediaTypeUiModel.SERIES)
        testDispatcher.scheduler.advanceUntilIdle()

        val pagingData = viewModel.state.value.reviews
        val items = pagingData.asSnapshot()
        assertThat(items.take(dummyReviews.size)).isEqualTo(dummyReviews.map { it.toReviewUiModel() })
    }

    @Test
    fun `fetchReviews should set isLoading to true during fetch`() = runTest {
        coEvery { manageMovieDetails.getReviewsByMovieId(mediaId, any()) } coAnswers {
            assertThat(viewModel.state.value.isLoading).isTrue()
            dummyReviews
        }

        viewModel = givenHappyViewModel(MediaTypeUiModel.MOVIE)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `fetchReviews handles NoNetworkException correctly`() = runTest {
        coEvery {
            manageTvSeriesDetails.getTvSeriesReviews(
                mediaId,
                any()
            )
        } throws NoNetworkException()

        viewModel = ReviewViewModel(
            mediaId = mediaId,
            mediaType = MediaTypeUiModel.SERIES,
            manageMovieDetails = manageMovieDetails,
            manageTvSeriesDetails = manageTvSeriesDetails,
            dispatcher = testDispatcher
        )

        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    private fun givenHappyViewModel(type: MediaTypeUiModel): ReviewViewModel {
        return ReviewViewModel(
            mediaId = mediaId,
            mediaType = type,
            manageMovieDetails = manageMovieDetails,
            manageTvSeriesDetails = manageTvSeriesDetails,
            dispatcher = testDispatcher
        )
    }

    companion object {
        private val dummyReviews = listOf(
            Review(
                id = "1",
                authorName = "CinephileHub",
                userHandle = "MovieBuff1967",
                content = "A poetic exploration of adolescence.",
                rating = 9.8f,
                createdDate = LocalDate.parse("2001-12-03"),
                avatarUrl = ""
            ),
            Review(
                id = "2",
                authorName = "CritiqueMaster",
                userHandle = "SharpEye",
                content = "Visually stunning but narratively weak.",
                rating = 6.5f,
                createdDate = LocalDate.parse("2002-05-17"),
                avatarUrl = ""
            )
        )
    }
}