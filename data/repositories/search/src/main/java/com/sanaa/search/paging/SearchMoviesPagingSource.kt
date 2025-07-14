package com.sanaa.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.mapper.toDtoId
import com.sanaa.search.mapper.toSearchOutput
import usecase.search.MediaFilters
import usecase.search.SearchMediaOutput

class SearchMoviesPagingSource(
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
            
            val response = remoteDataSource.searchMovies(query, page)
            
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
    
    private fun applyFilters(movies: List<MovieSearchDto>, filters: MediaFilters): List<MovieSearchDto> {
        var filteredMovies = movies
        
        if (filters.genres.isNotEmpty()) {
            val filterGenreIds = filters.genres.map { it.toDtoId() }
            filteredMovies = filteredMovies.filter { movie ->
                movie.genreIds?.any { it in filterGenreIds } == true
            }
        }
        
        filters.imdbRating?.let { rating ->
            filteredMovies = filteredMovies.filter { movie ->
                (movie.voteAverage ?: 0f) >= rating
            }
        }
        
        filters.startYear?.let { year ->
            filteredMovies = filteredMovies.filter { movie ->
                movie.releaseDate != null && 
                kotlinx.datetime.LocalDate.parse(movie.releaseDate).year >= year
            }
        }
        
        filters.endYear?.let { year ->
            filteredMovies = filteredMovies.filter { movie ->
                movie.releaseDate != null && 
                kotlinx.datetime.LocalDate.parse(movie.releaseDate).year <= year
            }
        }
        
        return filteredMovies
    }
    
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
} 