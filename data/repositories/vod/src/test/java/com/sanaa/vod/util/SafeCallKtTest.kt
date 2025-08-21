package com.sanaa.vod.util

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.util.exceptions.ConnectionException
import com.sanaa.vod.util.exceptions.TimeoutException
import exceptions.NoNetworkException
import exceptions.NovixAppException
import org.junit.jupiter.api.Test

class SafeCallTest {

    @Test
    fun `returns result when block succeeds`() {
        val result = runCatching {
            safeCall("Error") { "Success" }
        }

        assertThat(result.getOrNull()).isEqualTo("Success")
    }

    @Test
    fun `throws NovixAppException when block throws Exception`() {
        val result = runCatching {
            safeCall("Error") { throw TimeoutException("timeout!") }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(NovixAppException::class.java)
    }

    @Test
    fun `throws NoNetworkException when catch ConnectionException`() {
        val result = runCatching {
            safeCall("Error") { throw ConnectionException() }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(NoNetworkException::class.java)
    }
}