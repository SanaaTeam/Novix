package com.sanaa.presentation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import search.usecase.SearchUseCase
import search.usecase.search_param.SearchActorOutput

class SearchActorsPagingSource(
    private val searchUseCase: SearchUseCase,
    private val query: String,
) : PagingSource<Int, SearchActorOutput>() {

    override fun getRefreshKey(state: PagingState<Int, SearchActorOutput>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchActorOutput> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val data = searchUseCase.searchActors(query = query, page = page)

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}