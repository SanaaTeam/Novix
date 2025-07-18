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
import search.usecase.SearchActorsUseCase
import search.usecase.search_param.SearchActorOutput

class SearchActorsPagingSourceTest {
    private val searchActorsUseCase: SearchActorsUseCase = mockk(relaxed = true)
    private val query: String = "Actor1"
    private lateinit var searchActorsPagingSource: SearchActorsPagingSource

    @BeforeEach
    fun setUp() {
        searchActorsPagingSource = SearchActorsPagingSource(searchActorsUseCase, query)
    }

    @Test
    fun `getRefreshKey() should return null when there is no anchorPosition`() {
        val state = getPageState(anchorPosition = null)
        val refreshKey = searchActorsPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(null)
    }

    @Test
    fun `getRefreshKey() should return null when there is no prevKey or nextKey`() {
        val state = getPageState(anchorPosition = 0)
        val refreshKey = searchActorsPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(null)
    }

    @Test
    fun `getRefreshKey() should return null when there is data`() {
        val state = getPageState(anchorPosition = 0, data = emptyList())
        val refreshKey = searchActorsPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(null)
    }

    @Test
    fun `getRefreshKey() should return first page when there is no prevKey or nextKey`() {
        val state = getPageState(anchorPosition = 1, nextKey = 2)
        val refreshKey = searchActorsPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(1)
    }

    @Test
    fun `getRefreshKey() should return second page when after prevKey 1 with anchorPosition 0`() {
        val state = getPageState(anchorPosition = 0, prevKey = 1)
        val refreshKey = searchActorsPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(2)
    }

    @Test
    fun `getRefreshKey() should return first page when after nextKey 2 with anchorPosition 0`() {
        val state = getPageState(anchorPosition = 0, nextKey = 2)
        val refreshKey = searchActorsPagingSource.getRefreshKey(state)
        Truth.assertThat(refreshKey).isEqualTo(1)
    }

    @Test
    fun `load() should returns correct data when successful`() = runTest {
        val mockData = FakeData.actorOutputs
        val params = getPagingParams(key = null)
        coEvery { searchActorsUseCase.execute(query, 1) } returns mockData
        val result = searchActorsPagingSource.load(params)

        val page = result as LoadResult.Page
        Truth.assertThat(page.data).isEqualTo(mockData)
    }

    @Test
    fun `load() should returns empty list when there is no data`() = runTest {
        val mockData = FakeData.actorOutputs
        val params = getPagingParams(key = 2)
        coEvery { searchActorsUseCase.execute(query, 1) } returns mockData
        val result = searchActorsPagingSource.load(params)

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
        data: List<SearchActorOutput> = FakeData.actorOutputs,
    ): PagingState<Int, SearchActorOutput> {
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