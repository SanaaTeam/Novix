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

    override suspend fun searchMedia(
        query: String,
        filters: MediaFilters?,
        mediaType: MediaType
    ): List<SearchMediaOutput> {
        return try {
            if (mediaType == MediaType.MOVIE) {
                searchMovies(query, filters)
            } else {
                searchTvSeries(query, filters)
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
        filters: MediaFilters?
    ): List<SearchMediaOutput> {
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

                return filteredMedia.map { it.toSearchOutput(false) }

            } ?: return cachedMedia.map { it.toSearchOutput(false) }


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


                return filteredMedia.map {
                    it.toSearchOutput(false)
                }
            } ?: return movies.map { it.toSearchOutput(false) }

        }
    }

    private suspend fun searchTvSeries(
        query: String,
        filters: MediaFilters?
    ): List<SearchMediaOutput> {
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


                return filteredMedia.map { it.toSearchOutput(false) }

            } ?: return cachedTvSeries.map { it.toSearchOutput(false) }

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

                return filteredMedia.map {
                    it.toSearchOutput(false)
                }
            } ?: return tvSeries.map { it.toSearchOutput(false) }
        }
    }
}