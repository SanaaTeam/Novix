package com.sanaa.presentation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import entity.Actor
import usecase.search.SearchUseCase

class SearchActorsPagingSource(
    private val searchUseCase: SearchUseCase,
    private val query: String,
) : PagingSource<Int, Actor>() {

    override fun getRefreshKey(state: PagingState<Int, Actor>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Actor> {
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