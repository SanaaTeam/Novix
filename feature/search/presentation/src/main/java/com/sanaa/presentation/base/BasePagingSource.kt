package com.sanaa.presentation.base

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

class BasePagingSource<T : Any>(
    private val fetchItems: suspend (page: Int) -> List<T>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: STARTING_PAGE_INDEX
        Log.d("PagingSource", "Loading page: $page")

        return try {
            val items = fetchItems(page)
            Log.d("PagingSource", "Fetched ${items.size} items for page: $page")
            LoadResult.Page(
                data = items,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (items.isEmpty()) null else page.plus(1)
            )
        } catch (e: Exception) {
            Log.e("PagingSource", "Error loading page: $page", e)
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
