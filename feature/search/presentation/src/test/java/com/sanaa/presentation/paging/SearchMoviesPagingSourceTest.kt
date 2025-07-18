package com.sanaa.presentation.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.google.common.truth.Truth
import com.sanaa.presentation.fake.FakeData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import search.usecase.SearchMoviesUseCase
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchMovieOutput

class SearchMoviesPagingSourceTest {
    private val searchMoviesUseCase: SearchMoviesUseCase = mockk(relaxed = true)
    private val filters: MediaFilters = mockk(relaxed = true)
    private val query: String = "Movie1"
    private lateinit var searchMoviesPagingSource: SearchMoviesPagingSource

    @BeforeEach
    fun setUp() {
        searchMoviesPagingSource = SearchMoviesPagingSource(searchMoviesUseCase, query, filters)
    }

    @Test
    fun `getRefreshKey() should return null when there is no anchorPosition`() {
        val state = getPageState(anchorPosition = null)
        val refreshKey = searchMoviesPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(null)
    }

    @Test
    fun `getRefreshKey() should return null when there is no prevKey or nextKey`() {
        val state = getPageState(anchorPosition = 0)
        val refreshKey = searchMoviesPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(null)
    }

    @Test
    fun `getRefreshKey() should return null when there is data`() {
        val state = getPageState(anchorPosition = 0, data = emptyList())
        val refreshKey = searchMoviesPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(null)
    }

    @Test
    fun `getRefreshKey() should return first page when there is no prevKey or nextKey`() {
        val state = getPageState(anchorPosition = 1, nextKey = 2)
        val refreshKey = searchMoviesPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(1)
    }

    @Test
    fun `getRefreshKey() should return second page when after prevKey 1 with anchorPosition 0`() {
        val state = getPageState(anchorPosition = 0, prevKey = 1)
        val refreshKey = searchMoviesPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(2)
    }

    @Test
    fun `getRefreshKey() should return first page when after nextKey 2 with anchorPosition 0`() {
        val state = getPageState(anchorPosition = 0, nextKey = 2)
        val refreshKey = searchMoviesPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(1)
    }

    @Test
    fun `load() should returns correct data when successful`() = runTest {
        val mockData = FakeData.moviesOutput
        val params = getPagingParams(key = null)
        coEvery { searchMoviesUseCase.execute(query, 1, filters) } returns mockData
        val result = searchMoviesPagingSource.load(params)

        val page = result as LoadResult.Page
        Truth.assertThat(page.data).isEqualTo(mockData)
    }

    @Test
    fun `load() should returns empty list when there is no data`() = runTest {
        val mockData = FakeData.moviesOutput
        val params = getPagingParams(key = 2)
        coEvery { searchMoviesUseCase.execute(query, 1, filters) } returns mockData
        val result = searchMoviesPagingSource.load(params)

        val page = result as LoadResult.Page
        Truth.assertThat(page.data).isEqualTo(emptyList<SearchActorOutput>())
    }


    private fun getPagingParams(key: Int? = null): PagingSource.LoadParams<Int> {
        return PagingSource.LoadParams.Refresh(
            key = key,
            loadSize = 1,
            placeholdersEnabled = false
        )
    }

    private fun getPageState(
        anchorPosition: Int? = null,
        prevKey: Int? = null,
        nextKey: Int? = null,
        data: List<SearchMovieOutput> = FakeData.moviesOutput,
    ): PagingState<Int, SearchMovieOutput> {
        return PagingState(
            anchorPosition = anchorPosition,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0,
            pages = listOf(
                LoadResult.Page(
                    data = data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            ),
        )
    }
}