package com.sanaa.presentation.screens.review

import androidx.compose.ui.test.junit4.createComposeRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.screen.review.ReviewScreenEffects
import com.sanaa.presentation.screen.review.ReviewScreenInteractionListener
import com.sanaa.presentation.screen.review.ReviewViewModel
import details.usecase.ManageMovieDetailsUseCase
import details.usecase.ManageTvSeriesDetailsUseCase
import entity.Review
import exceptions.NoNetworkException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val manageMovieDetails: ManageMovieDetailsUseCase = mockk(relaxed = true)
    private val manageTvSeriesDetails: ManageTvSeriesDetailsUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ReviewViewModel
    private val mediaId = 101
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `ReviewScreenInteractionListener calls onBackClick`() {
        var backClicked = false
        val listener = object : ReviewScreenInteractionListener {
            override fun onBackClick() {
                backClicked = true
            }
        }

        listener.onBackClick()
        assertThat(backClicked).isTrue()
    }

    @Test
    fun `onBackClick emits NavigateBack`() = runTest {
        givenHappyViewModel(MediaTypeUiModel.MOVIE)
        viewModel.onBackClick()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ReviewScreenEffects.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchReviews returns empty list when no reviews found`() = runTest {
        coEvery { manageMovieDetails.getReviewsByMovieId(mediaId) } returns emptyList()
        givenHappyViewModel(MediaTypeUiModel.MOVIE)

        assertThat(viewModel.state.value.reviews).isEmpty()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `fetchReviews handles NoNetworkException correctly`() = runTest {
        coEvery { manageTvSeriesDetails.getTvSeriesReviews(mediaId) } throws NoNetworkException()

        viewModel = ReviewViewModel(
            mediaId = mediaId,
            mediaType = MediaTypeUiModel.SERIES,
            manageMovieDetails = manageMovieDetails,
            manageTvSeriesDetails = manageTvSeriesDetails
        )

        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `fetchReviews sets isLoading to true during execution`() = runTest {
        coEvery { manageMovieDetails.getReviewsByMovieId(mediaId) } coAnswers {
            assertThat(viewModel.state.value.isLoading).isTrue()
            dummyReviews
        }

        viewModel = ReviewViewModel(
            mediaId = mediaId,
            mediaType = MediaTypeUiModel.MOVIE,
            manageMovieDetails = manageMovieDetails,
            manageTvSeriesDetails = manageTvSeriesDetails
        )
    }

    private fun givenHappyViewModel(type: MediaTypeUiModel) {
        if (type == MediaTypeUiModel.MOVIE) {
            coEvery { manageMovieDetails.getReviewsByMovieId(mediaId) } returns dummyReviews
        } else {
            coEvery { manageTvSeriesDetails.getTvSeriesReviews(mediaId) } returns dummyReviews
        }

        viewModel = ReviewViewModel(
            mediaId = mediaId,
            mediaType = type,
            manageMovieDetails = manageMovieDetails,
            manageTvSeriesDetails = manageTvSeriesDetails
        )
    }

    companion object {
        private val dummyReviews = listOf(
            Review(
                id = 1,
                authorName = "CinephileHub",
                userHandle = "MovieBuff1967",
                content = "A poetic exploration of adolescence.",
                rating = 9.8f,
                createdDate = LocalDate.parse("2001-12-03"),
                avatarUrl = ""
            ),
            Review(
                id = 2,
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