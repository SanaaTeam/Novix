package com.sanaa.search.repository

import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.mapper.toLocalDto
import com.sanaa.search.mapper.toOutput
import exceptions.DataSourceAccessException
import exceptions.RetrievingDataFailureException
import repository.LanguageProvider
import repository.SearchRepository
import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.SearchActorOutput
import usecase.search.SearchMediaOutput
import java.net.UnknownHostException

class SearchRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localDataSource: LocalCacheSearchDataSource,
    private val languageProvider: LanguageProvider,
) : SearchRepository {
    override suspend fun searchActors(query: String): List<SearchActorOutput> {
        return try {
            val cachedActors = localDataSource.getActorsByQuery(query)
            if (cachedActors.isNotEmpty()) {
                cachedActors.toOutput()
            } else {
                val actors = remoteDataSource.searchActors(query).results.also {
                    it.forEach {
                        localDataSource.cacheActor(
                            it.toLocalDto(languageProvider.getCurrentLanguage())
                        )
                    }
                }
                actors.toOutput()
            }
        } catch (_: UnknownHostException) {
            throw DataSourceAccessException("No network connection available")
        } catch (_: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve actors for query: $query")
        }
    }

    override suspend fun searchMedia(
        query: String,
        filters: MediaFilters?,
        mediaType: MediaType
    ): List<SearchMediaOutput> {
        TODO("Not yet implemented")
    }
}