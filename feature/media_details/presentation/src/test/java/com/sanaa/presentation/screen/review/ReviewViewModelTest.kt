package com.sanaa.presentation.screen.review

import com.google.common.truth.Truth.assertThat
import entity.Review
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
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
}