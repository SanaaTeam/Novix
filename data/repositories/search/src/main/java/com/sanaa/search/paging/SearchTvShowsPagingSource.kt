package com.sanaa.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import com.sanaa.search.mapper.toDtoId
import com.sanaa.search.mapper.toSearchOutput
import usecase.search.MediaFilters
import usecase.search.SearchMediaOutput

class SearchTvShowsPagingSource(
    private val remoteDataSource: SearchRemoteDataSource,
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
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX
            
            val response = remoteDataSource.searchTv(query, page)
            
            val filteredResults = if (filters != null) {
                applyFilters(response.results, filters)
            } else {
                response.results
            }
            
            val searchOutputs = filteredResults.map { it.toSearchOutput(false) }
            
            LoadResult.Page(
                data = searchOutputs,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page >= response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
    
    private fun applyFilters(tvShows: List<TvShowSearchDto>, filters: MediaFilters): List<TvShowSearchDto> {
        var filteredTvShows = tvShows
        
        if (filters.genres.isNotEmpty()) {
            val filterGenreIds = filters.genres.map { it.toDtoId() }
            filteredTvShows = filteredTvShows.filter { tvShow ->
                tvShow.genreIds?.any { it in filterGenreIds } == true
            }
        }
        
        filters.imdbRating?.let { rating ->
            filteredTvShows = filteredTvShows.filter { tvShow ->
                (tvShow.voteAverage ?: 0f) >= rating
            }
        }
        
        filters.startYear?.let { year ->
            filteredTvShows = filteredTvShows.filter { tvShow ->
                tvShow.releaseDate != null &&
                kotlinx.datetime.LocalDate.parse(tvShow.releaseDate).year >= year
            }
        }
        
        filters.endYear?.let { year ->
            filteredTvShows = filteredTvShows.filter { tvShow ->
                tvShow.releaseDate != null &&
                kotlinx.datetime.LocalDate.parse(tvShow.releaseDate).year <= year
            }
        }
        
        return filteredTvShows
    }
    
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
} 