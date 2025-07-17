package com.sanaa.search.repository

import android.util.Log
import com.example.env_config.service.LanguageProvider
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.mapper.toDtoId
import com.sanaa.search.mapper.toLocalDto
import com.sanaa.search.mapper.toSearchOutput
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import kotlinx.datetime.LocalDate
import search.repository.SearchRepository
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchMediaOutput
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

class SearchRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localDataSource: LocalCacheSearchDataSource,
    private val languageProvider: LanguageProvider,
) : SearchRepository {

    override suspend fun searchActors(query: String, page: Int): List<SearchActorOutput> {
        try {
            val pageSize = 20
            val offset = (page - 1) * pageSize
            Log.d("SearchRepo", "Page: $page, PageSize: $pageSize, Offset: $offset")

            val cachedActors = localDataSource.getPagedActorsByQuery(query, pageSize, offset)

            if (cachedActors.isNotEmpty()) {
                return cachedActors.map { it.toSearchOutput() }
            } else {
                val actors = remoteDataSource.searchActors(query, page).results.also {
                    it.forEach {
                        localDataSource.cacheActor(
                            it.toLocalDto(languageProvider.getCurrentLanguage())
                        )
                    }
                }
                return actors.map { it.toSearchOutput() }
            }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (_: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve actors for query: $query")
        }
    }

    override suspend fun searchMedia(
        query: String,
        page: Int,
        filters: MediaFilters?,
        mediaType: MediaType
    ): List<SearchMediaOutput> {
        return try {
            if (mediaType == MediaType.MOVIE) {
                searchMovies(query, filters, page)
            } else {
                searchTvSeries(query, filters, page)
            }
        } catch (_: UnresolvedAddressException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RetrievingDataFailureException("Failed to retrieve media for query: $query")
        }
    }

    private suspend fun searchMovies(
        query: String,
        filters: MediaFilters?,
        page: Int
    ): List<SearchMediaOutput> {
        val pageSize = 20
        val offset = (page - 1) * pageSize
        Log.d("SearchRepo", "Movies -> Page: $page, PageSize: $pageSize, Offset: $offset")

        val cachedMedia = localDataSource.getMoviesByQuery(query, limit = pageSize, offset = offset)

        if (cachedMedia.isNotEmpty()) {
            return applyFiltersToLocalMovies(cachedMedia, filters)
        } else {
            val movies = remoteDataSource.searchMovies(query, page).results.also {
                it.forEach {
                    localDataSource.cacheMovie(
                        it.toLocalDto(languageProvider.getCurrentLanguage())
                    )
                }
            }
            return applyFiltersToRemoteMovies(movies, filters)
        }
    }

    private suspend fun searchTvSeries(
        query: String,
        filters: MediaFilters?,
        page: Int
    ): List<SearchMediaOutput> {
        val pageSize = 20
        val offset = (page - 1) * pageSize
        Log.d("SearchRepo", "TvSeries -> Page: $page, PageSize: $pageSize, Offset: $offset")

        val cachedTvSeries = localDataSource.getTvSeriesByQuery(query, limit = pageSize, offset = offset)
        
        if (cachedTvSeries.isNotEmpty()) {
            return applyFiltersToLocalTvSeries(cachedTvSeries, filters)
        } else {
            val tvSeries = remoteDataSource.searchTv(query, page).results.also {
                it.forEach {
                    localDataSource.cacheTvSeries(
                        it.toLocalDto(languageProvider.getCurrentLanguage())
                    )
                }
            }
            return applyFiltersToRemoteTvSeries(tvSeries, filters)
        }
    }

    private fun applyFiltersToLocalMovies(
        movies: List<MoviesLocalDto>,
        filters: MediaFilters?
    ): List<SearchMediaOutput> {
        if (filters == null) {
            return movies.map { it.toSearchOutput(false) }
        }

        return movies.asSequence()
            .filter { movie ->
                val hasMatchingGenre = filters.genres.isEmpty() || 
                    movie.genres?.split(", ")?.any { genreId ->
                        genreId.toIntOrNull()?.let { it in filters.genres.map { it.toDtoId() } } ?: false
                    } == true

                val meetsRatingCriteria = filters.imdbRating?.let { rating ->
                    (movie.imdbRating ?: 0f) >= rating
                } ?: true

                val meetsYearCriteria = when {
                    filters.startYear != null && filters.endYear != null -> {
                        movie.releaseYear?.let { year ->
                            year in filters.startYear!!..filters.endYear!!
                        } ?: false
                    }
                    filters.startYear != null -> {
                        movie.releaseYear?.let { it >= filters.startYear!! } ?: false
                    }
                    filters.endYear != null -> {
                        movie.releaseYear?.let { it <= filters.endYear!! } ?: false
                    }
                    else -> true
                }

                hasMatchingGenre && meetsRatingCriteria && meetsYearCriteria
            }
            .map { it.toSearchOutput(false) }
            .toList()
    }

    private fun applyFiltersToLocalTvSeries(
        tvSeries: List<TvSeriesLocalDto>,
        filters: MediaFilters?
    ): List<SearchMediaOutput> {
        if (filters == null) {
            return tvSeries.map { it.toSearchOutput(false) }
        }

        return tvSeries.asSequence()
            .filter { series ->
                val hasMatchingGenre = filters.genres.isEmpty() || 
                    series.genres?.split(", ")?.any { genreId ->
                        genreId.toIntOrNull()?.let { it in filters.genres.map { it.toDtoId() } } ?: false
                    } == true

                val meetsRatingCriteria = filters.imdbRating?.let { rating ->
                    (series.imdbRating ?: 0f) >= rating
                } ?: true

                val meetsYearCriteria = when {
                    filters.startYear != null && filters.endYear != null -> {
                        series.releaseYear?.let { year ->
                            year in filters.startYear!!..filters.endYear!!
                        } ?: false
                    }
                    filters.startYear != null -> {
                        series.releaseYear?.let { it >= filters.startYear!! } ?: false
                    }
                    filters.endYear != null -> {
                        series.releaseYear?.let { it <= filters.endYear!! } ?: false
                    }
                    else -> true
                }

                hasMatchingGenre && meetsRatingCriteria && meetsYearCriteria
            }
            .map { it.toSearchOutput(false) }
            .toList()
    }

    private fun applyFiltersToRemoteMovies(
        movies: List<com.sanaa.search.dataSource.remote.dto.MovieSearchDto>,
        filters: MediaFilters?
    ): List<SearchMediaOutput> {
        if (filters == null) {
            return movies.map { it.toSearchOutput(false) }
        }

        return movies.asSequence()
            .filter { movie ->
                val hasMatchingGenre = filters.genres.isEmpty() || 
                    movie.genreIds?.any { it in filters.genres.map { it.toDtoId() } } == true

                val meetsRatingCriteria = filters.imdbRating?.let { rating ->
                    (movie.voteAverage ?: 0f) >= rating
                } ?: true

                val meetsYearCriteria = when {
                    filters.startYear != null && filters.endYear != null -> {
                        movie.releaseDate?.let { date ->
                            try {
                                val year = LocalDate.parse(date).year
                                year in filters.startYear!!..filters.endYear!!
                            } catch (e: Exception) {
                                false
                            }
                        } ?: false
                    }
                    filters.startYear != null -> {
                        movie.releaseDate?.let { date ->
                            try {
                                LocalDate.parse(date).year >= filters.startYear!!
                            } catch (e: Exception) {
                                false
                            }
                        } ?: false
                    }
                    filters.endYear != null -> {
                        movie.releaseDate?.let { date ->
                            try {
                                LocalDate.parse(date).year <= filters.endYear!!
                            } catch (e: Exception) {
                                false
                            }
                        } ?: false
                    }
                    else -> true
                }

                hasMatchingGenre && meetsRatingCriteria && meetsYearCriteria
            }
            .map { it.toSearchOutput(false) }
            .toList()
    }

    private fun applyFiltersToRemoteTvSeries(
        tvSeries: List<com.sanaa.search.dataSource.remote.dto.TvShowSearchDto>,
        filters: MediaFilters?
    ): List<SearchMediaOutput> {
        if (filters == null) {
            return tvSeries.map { it.toSearchOutput(false) }
        }

        return tvSeries.asSequence()
            .filter { series ->
                val hasMatchingGenre = filters.genres.isEmpty() || 
                    series.genreIds?.any { it in filters.genres.map { it.toDtoId() } } == true

                val meetsRatingCriteria = filters.imdbRating?.let { rating ->
                    (series.voteAverage ?: 0f) >= rating
                } ?: true

                val meetsYearCriteria = when {
                    filters.startYear != null && filters.endYear != null -> {
                        series.releaseDate?.let { date ->
                            try {
                                val year = LocalDate.parse(date).year
                                year in filters.startYear!!..filters.endYear!!
                            } catch (e: Exception) {
                                false
                            }
                        } ?: false
                    }
                    filters.startYear != null -> {
                        series.releaseDate?.let { date ->
                            try {
                                LocalDate.parse(date).year >= filters.startYear!!
                            } catch (e: Exception) {
                                false
                            }
                        } ?: false
                    }
                    filters.endYear != null -> {
                        series.releaseDate?.let { date ->
                            try {
                                LocalDate.parse(date).year <= filters.endYear!!
                            } catch (e: Exception) {
                                false
                            }
                        } ?: false
                    }
                    else -> true
                }

                hasMatchingGenre && meetsRatingCriteria && meetsYearCriteria
            }
            .map { it.toSearchOutput(false) }
            .toList()
    }
}