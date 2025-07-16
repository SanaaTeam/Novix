package com.sanaa.presentation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import search.usecase.SearchMoviesUseCase
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchMediaOutput

class SearchMoviesPagingSource(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val query: String,
    private val filters: MediaFilters?
) : PagingSource<Int, SearchMediaOutput>() {

    override fun getRefreshKey(state: PagingState<Int, SearchMediaOutput>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchMediaOutput> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val results = searchMoviesUseCase.execute(query = query, page = page, filters = filters)

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
