package com.sanaa.presentation.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import exceptions.NovixAppException

class BasePagingSourceForHome<T : Any>(
    private val onError: ((NovixAppException) -> Unit)? = null,
    private val fetchItems: suspend (page: Int) -> List<T>,
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return try {
            val items = fetchItems(page)
            LoadResult.Page(
                data = items,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (items.isEmpty()) null else page.plus(1)
            )
        } catch (e: Exception) {
            onError?.invoke(
                when(e){
                    is NovixAppException -> e
                    else -> NovixAppException(e.message)
                }
            )
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
