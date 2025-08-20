package com.sanaa.presentation.homeBase

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BasePagingSourceTest {

    private val sampleData = listOf("A", "B", "C")

    private var pagingSource = BasePagingSourceForHome { page: Int ->
        when (page) {
            1 -> sampleData
            2 -> emptyList()
            else -> throw RuntimeException("Unexpected page")
        }
    }

    private fun getPagingParams(key: Int? = 1): PagingSource.LoadParams<Int> {
        return PagingSource.LoadParams.Refresh(
            key = key,
            loadSize = 10,
            placeholdersEnabled = false
        )
    }

    @BeforeEach
    fun setUp() {
        pagingSource = BasePagingSourceForHome { page ->
            when (page) {
                1 -> sampleData
                else -> emptyList()
            }
        }
    }

    @Test
    fun `getRefreshKey() should return null when anchorPosition is null`() {
        val state = getPageState(anchorPosition = null)
        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isEqualTo(null)
    }

    @Test
    fun `getRefreshKey() should return null when no prevKey or nextKey`() {
        val state = getPageState(anchorPosition = 0)
        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isEqualTo(null)
    }

    @Test
    fun `getRefreshKey() should return correct key when prevKey exists`() {
        val state = getPageState(anchorPosition = 0, prevKey = 1)
        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isEqualTo(2)
    }

    @Test
    fun `getRefreshKey() should return correct key when nextKey exists`() {
        val state = getPageState(anchorPosition = 0, nextKey = 2)
        val refreshKey = pagingSource.getRefreshKey(state)
        assertThat(refreshKey).isEqualTo(1)
    }

    @Test
    fun `load() should return Page with data`() = runTest {
        val params = getPagingParams(key = 1)
        val result = pagingSource.load(params) as LoadResult.Page

        assertThat(result.data).isEqualTo(sampleData)
        assertThat(result.prevKey).isEqualTo(null)
        assertThat(result.nextKey).isEqualTo(2)
    }

    @Test
    fun `load() should return Page with empty data`() = runTest {
        val params = getPagingParams(key = 2)
        val result = pagingSource.load(params) as LoadResult.Page

        assertThat(result.data).isEmpty()
        assertThat(result.prevKey).isEqualTo(1)
        assertThat(result.nextKey).isEqualTo(null)
    }

    @Test
    fun `load() should return Error when exception is thrown`() = runTest {
        val errorPagingSource =
            BasePagingSourceForHome<String> { throw RuntimeException("Failure") }
        val result = errorPagingSource.load(getPagingParams())

        assertThat(result).isInstanceOf(LoadResult.Error::class.java)
        val error = result as LoadResult.Error
        assertThat(error.throwable.message).isEqualTo("Failure")
    }
    // Helper methods

    private fun getPageState(
        anchorPosition: Int? = null,
        prevKey: Int? = null,
        nextKey: Int? = null,
        data: List<String> = sampleData,
    ): PagingState<Int, String> {
        return PagingState(
            pages = listOf(
                LoadResult.Page(
                    data = data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            ),
            anchorPosition = anchorPosition,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )
    }
}