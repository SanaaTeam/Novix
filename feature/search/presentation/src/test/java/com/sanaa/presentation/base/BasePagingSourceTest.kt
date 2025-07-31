package com.sanaa.presentation.base

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import repository.SearchRepository.SearchResult

class BasePagingSourceTest {

    private val sampleData = listOf("A", "B", "C")

    private var pagingSource = BasePagingSource { page: Int ->
        when (page) {
            1 -> SearchResult(1, sampleData)
            2 -> SearchResult(2, emptyList())
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
        assertThat(result.nextKey).isEqualTo(null)
    }

    @Test
    fun `load() should return Page with data when first page returns empty list and there are available data in second page`() = runTest {
        val pagingSource = BasePagingSource { page: Int ->
            when (page) {
                1 -> SearchResult(2, emptyList())
                2 -> SearchResult(2, sampleData)
                else -> throw RuntimeException("Unexpected page")
            }
        }
        val params = getPagingParams(key = 1)
        val result = pagingSource.load(params) as LoadResult.Page


        assertThat(result.data).isEqualTo(sampleData)
        assertThat(result.prevKey).isEqualTo(null)
        assertThat(result.nextKey).isEqualTo(null)
    }

    @Test
    fun `load() should return Error when exception is thrown`() = runTest {
        val errorPagingSource = BasePagingSource<String> { throw RuntimeException("Failure") }
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
