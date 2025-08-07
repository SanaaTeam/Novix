package com.sanaa.vod.repository.service

import android.content.Context
import com.sanaa.identity.R
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
        // Setup mock responses for getString
        every { context.getString(R.string.no_internet_connection_error) } returns "No internet connection."
        every { context.getString(R.string.something_went_wrong_error) } returns "Something went wrong."

        stringProvider = VodStringProviderImpl(context)
    }

    @Test
    fun `noInternetConnectionError returns expected string`() {
        assertEquals("No internet connection.", stringProvider.noInternetConnectionError)
    }

    @Test
    fun `somethingWentWrongError returns expected string`() {
        assertEquals("Something went wrong.", stringProvider.somethingWentWrongError)
    }
}
