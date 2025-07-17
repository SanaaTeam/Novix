package com.sanaa.search.repository

import com.example.env_config.service.LanguageProvider
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.mapper.toDtoId
import com.sanaa.search.mapper.toLocalDto
import com.sanaa.search.mapper.toSearchOutput
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import kotlinx.datetime.LocalDate
import search.repository.SearchRepository
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchMovieOutput
import search.usecase.search_param.SearchTvSeriesOutput
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

class SearchRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localDataSource: LocalCacheSearchDataSource,
    private val languageProvider: LanguageProvider,
) : SearchRepository {
    override suspend fun searchActors(query: String): List<SearchActorOutput> {
        try {
            val cachedActors = localDataSource.getActorsByQuery(query)
            if (cachedActors.isNotEmpty()) {
                return cachedActors.map { it.toSearchOutput() }
            } else {
                val actors = remoteDataSource.searchActors(query).results.also {
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

    override suspend fun searchMovies(
        query: String,
        filters: MediaFilters?,
    ): List<SearchMovieOutput> = searchOrThrow(query) {
        val cachedMedia = localDataSource.getMoviesByQuery(query)
        if (cachedMedia.isNotEmpty()) {
            filters?.let {
                val filterGenreIds = filters.genres.map { it.toDtoId() }
                var filteredMedia = cachedMedia.filter { media ->
                    val mediaGenreIds = media.genres
                        ?.split(", ")
                        ?.mapNotNull { it.toIntOrNull() }
                        ?.toSet() ?: emptySet()

                    filterGenreIds.all { it in mediaGenreIds }
                }

                filteredMedia =
                    filteredMedia.filter {
                        (it.imdbRating ?: 0f) >= filters.imdbRating
                                && it.releaseYear != null
                                && it.releaseYear >= filters.startYear
                                && it.releaseYear <= filters.endYear
                    }

                filteredMedia.map { it.toSearchOutput() }

            } ?: cachedMedia.map { it.toSearchOutput() }


        } else {
            val movies = remoteDataSource.searchMovies(query).results.also {
                it.forEach {
                    localDataSource.cacheMovie(
                        it.toLocalDto(languageProvider.getCurrentLanguage())
                    )
                }
            }

            filters?.let {
                val filterGenreIds = filters.genres.map { it.toDtoId() }
                var filteredMedia =
                    movies.filter { series ->
                        val genreIds = series.genreIds?.toSet() ?: emptySet()
                        filterGenreIds.all { it in genreIds }
                    }

                filteredMedia = filteredMedia.filter {
                    (it.voteAverage ?: 0f) >= filters.imdbRating
                            && it.releaseDate != null &&
                            LocalDate.parse(it.releaseDate).year >= filters.startYear
                            && LocalDate.parse(it.releaseDate).year <= filters.endYear
                }


                filteredMedia.map {
                    it.toSearchOutput()
                }
            } ?: movies.map { it.toSearchOutput() }

        }
    }

   override suspend fun searchTvShows(
        query: String,
        filters: MediaFilters?,
    ): List<SearchTvSeriesOutput> = searchOrThrow(query) {
        val cachedTvSeries = localDataSource.getTvSeriesByQuery(query)
        if (cachedTvSeries.isNotEmpty()) {
            filters?.let {
                val filterGenreIds = filters.genres.map { it.toDtoId() }
                var filteredMedia =
                    cachedTvSeries.filter { media ->
                        val mediaGenreIds = media.genres
                            ?.split(", ")
                            ?.mapNotNull { it.toIntOrNull() }
                            ?.toSet() ?: emptySet()

                        filterGenreIds.all { it in mediaGenreIds }
                    }

                filteredMedia =
                    filteredMedia.filter {
                        (it.imdbRating ?: 0f) >= filters.imdbRating
                                && it.releaseYear != null
                                && it.releaseYear >= filters.startYear
                                && it.releaseYear <= filters.endYear
                    }


                filteredMedia.map { it.toSearchOutput() }

            } ?: cachedTvSeries.map { it.toSearchOutput() }

        } else {
            val tvSeries = remoteDataSource.searchTv(query).results.also {
                it.forEach {
                    localDataSource.cacheTvSeries(
                        it.toLocalDto(languageProvider.getCurrentLanguage())
                    )
                }
            }
            filters?.let {
                val filterGenreIds = filters.genres.map { it.toDtoId() }
                var filteredMedia =
                    tvSeries.filter { series ->
                        val genreIds = series.genreIds?.toSet() ?: emptySet()
                        filterGenreIds.all { it in genreIds }
                    }

                filteredMedia = filteredMedia.filter {
                    (it.voteAverage ?: 0f) >= filters.imdbRating
                            && it.releaseDate != null &&
                            LocalDate.parse(it.releaseDate).year >= filters.startYear
                            && LocalDate.parse(it.releaseDate).year <= filters.endYear
                }

                filteredMedia.map {
                    it.toSearchOutput()
                }
            } ?: tvSeries.map { it.toSearchOutput() }
        }
    }

    private suspend fun <T> searchOrThrow(query: String, callee: suspend () -> T): T {
        try {
            return callee()
        } catch (_: UnresolvedAddressException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve data for query: $query")
        }
    }
}