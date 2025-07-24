package com.sanaa.vod.util

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.util.exceptions.ConnectionException
import com.sanaa.vod.util.exceptions.ParsingException
import com.sanaa.vod.util.exceptions.ServerErrorException
import com.sanaa.vod.util.exceptions.TimeoutException
import com.sanaa.vod.util.exceptions.UnknownDataSourceException
import exceptions.FailedToAddException
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
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
    fun `throws RetrievingDataFailureException when TimeoutException is thrown`() {
        val result = runCatching {
            safeCall("Error") { throw TimeoutException("timeout!") }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(RetrievingDataFailureException::class.java)
    }

    @Test
    fun `throws correct message when TimeoutException is thrown`() {
        val result = runCatching {
            safeCall("Error") { throw TimeoutException("timeout!") }
        }

        assertThat(result.exceptionOrNull()?.message).isEqualTo("timeout!")
    }

    @Test
    fun `throws RetrievingDataFailureException when ServerErrorException is thrown`() {
        val result = runCatching {
            safeCall("Error") { throw ServerErrorException("server issue") }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(RetrievingDataFailureException::class.java)
    }

    @Test
    fun `throws correct message when ServerErrorException is thrown`() {
        val result = runCatching {
            safeCall("Error") { throw ServerErrorException("server issue") }
        }

        assertThat(result.exceptionOrNull()?.message).isEqualTo("server issue")
    }

    @Test
    fun `throws RetrievingDataFailureException when ParsingException is thrown`() {
        val result = runCatching {
            safeCall("Error") { throw ParsingException("bad json") }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(RetrievingDataFailureException::class.java)
    }

    @Test
    fun `throws NoNetworkException when catch ConnectionException`() {
        val result = runCatching {
            safeCall("Error") { throw ConnectionException() }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(NoNetworkException::class.java)
    }

    @Test
    fun `throws correct message when ParsingException is thrown`() {
        val result = runCatching {
            safeCall("Error") { throw ParsingException("bad json") }
        }

        assertThat(result.exceptionOrNull()?.message).isEqualTo("bad json")
    }

    @Test
    fun `throws RetrievingDataFailureException when UnknownDataSourceException is thrown`() {
        val result = runCatching {
            safeCall("Error") { throw UnknownDataSourceException("???") }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(RetrievingDataFailureException::class.java)
    }

    @Test
    fun `throws correct message when UnknownDataSourceException is thrown`() {
        val result = runCatching {
            safeCall("Error") { throw UnknownDataSourceException("???") }
        }

        assertThat(result.exceptionOrNull()?.message).isEqualTo("???")
    }

    @Test
    fun `uses default exceptionProvider for unknown exceptions`() {
        val result = runCatching {
            safeCall("Failure reason") { throw IllegalStateException("something bad happened") }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(RetrievingDataFailureException::class.java)
    }

    @Test
    fun `throws message composed with default exceptionProvider`() {
        val result = runCatching {
            safeCall("Failure reason") { throw IllegalStateException("something bad happened") }
        }

        assertThat(result.exceptionOrNull()?.message).isEqualTo("Failure reason: something bad happened")
    }

    @Test
    fun `uses custom exceptionProvider`() {
        val result = runCatching {
            safeCall("Custom error", exceptionProvider = { msg -> FailedToAddException(msg) }) {
                throw IllegalArgumentException("add failed")
            }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(FailedToAddException::class.java)
    }

}