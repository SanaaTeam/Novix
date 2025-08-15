package com.sanaa.presentation.screen.review

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.ManageTvShowUseCase
import usecase.manageMovieUseCase.GetMovieDetailsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var manageTvShowDetails: ManageTvShowUseCase

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMovieDetailsUseCase = mockk(relaxed = true)
        manageTvShowDetails = mockk(relaxed = true)
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