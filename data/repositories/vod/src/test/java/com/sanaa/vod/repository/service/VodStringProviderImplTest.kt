package com.sanaa.vod.repository.service

import android.content.Context
import com.sanaa.data.repositories.vod.R
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.VodStringProvider

class VodStringProviderImplTest {

    private val context: Context = mockk(relaxed = true)
    private lateinit var stringProvider: VodStringProvider

    @BeforeEach
    fun setUp() {
        every { context.getString(R.string.no_internet_connection_error) } returns NO_INTERNET_CONNECTION_ERROR
        every { context.getString(R.string.something_went_wrong_error) } returns SOMETHING_WENT_WRONG_ERROR
        every { context.getString(R.string.add_to_list_failed) } returns ADD_TO_LIST_FAILED
        every { context.getString(R.string.add_to_list_success) } returns ADD_TO_LIST_SUCCESS

        stringProvider = VodStringProviderImpl(context)
    }

    @Test
    fun `noInternetConnectionError should return expected string`() {
        assertEquals("No internet connection.", stringProvider.noInternetConnectionError)
    }

    @Test
    fun `somethingWentWrongError should return expected string`() {
        assertEquals(SOMETHING_WENT_WRONG_ERROR, stringProvider.somethingWentWrongError)
    }

    @Test
    fun `addToListFailed should return expected string`() {
        assertEquals(ADD_TO_LIST_FAILED, stringProvider.addToListFailed)
    }

    @Test
    fun `addToListSuccess should return expected string`() {
        assertEquals(ADD_TO_LIST_SUCCESS, stringProvider.addToListSuccess)
    }

    private companion object {
        const val NO_INTERNET_CONNECTION_ERROR = "No internet connection."
        const val SOMETHING_WENT_WRONG_ERROR = "Something went wrong."
        const val ADD_TO_LIST_FAILED = "Failed to add to list."
        const val ADD_TO_LIST_SUCCESS = "Successfully added to list."}
}
