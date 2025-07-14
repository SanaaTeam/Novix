package com.sanaa.search.paging


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.mapper.toSearchOutput
import usecase.search.SearchActorOutput

class SearchActorsPagingSource(
    private val remoteDataSource: SearchRemoteDataSource,
    private val query: String
) : PagingSource<Int, SearchActorOutput>() {

    override fun getRefreshKey(state: PagingState<Int, SearchActorOutput>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchActorOutput> {
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX
            
            val response = remoteDataSource.searchActors(query, page)
            
            val searchOutputs = response.results.map { it.toSearchOutput() }
            
            LoadResult.Page(
                data = searchOutputs,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page >= response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
    
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
} 