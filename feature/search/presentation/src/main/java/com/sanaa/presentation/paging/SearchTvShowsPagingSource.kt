package com.sanaa.presentation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import search.usecase.SearchTvSeriesUseCase
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchTvSeriesOutput

class SearchTvShowsPagingSource(
    private val searchTvShowsUseCase: SearchTvSeriesUseCase,
    private val query: String,
    private val filters: MediaFilters?
) : PagingSource<Int, SearchTvSeriesOutput>() {

    override fun getRefreshKey(state: PagingState<Int, SearchTvSeriesOutput>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchTvSeriesOutput> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return try {
            val results = searchTvShowsUseCase.execute(query, page, filters)

            LoadResult.Page(
                data = results,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
