package com.sanaa.presentation.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import repository.SearchRepository.SearchResult

class BasePagingSource<T : Any>(
    private val fetchItems: suspend (page: Int) -> SearchResult<T>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: STARTING_PAGE_INDEX
        val minItems = 20
        val accumulatedItems = mutableListOf<T>()
        var currentPage = page
        var totalPageNumber: Int? = null

        try {
            do {
                val response = fetchItems(currentPage)
                totalPageNumber = response.totalPages
                accumulatedItems.addAll(response.results)
                if (accumulatedItems.size >= minItems || currentPage == response.totalPages) break
                currentPage++
            } while (true)
            return LoadResult.Page(
                data = accumulatedItems,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (totalPageNumber == -1 || (currentPage != totalPageNumber && accumulatedItems.size >= minItems))
                    currentPage.plus(1) else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
