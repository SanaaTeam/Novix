package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.local.history.LocalHistoryDataSource
import com.sanaa.vod.repository.mapper.history.toDto
import com.sanaa.vod.repository.mapper.history.toEntity
import com.sanaa.vod.util.safeCall
import entity.MediaHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import repository.HistoryRepository
import usecase.history.history_param.SearchHistory
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia
import usecase.search.search_param.MediaType
import javax.inject.Inject


class HistoryRepositoryImpl @Inject constructor(
    private val local: LocalHistoryDataSource
) : HistoryRepository {

    override suspend fun getSearchHistory(sizeLimit: Int): Flow<List<SearchHistory>> = safeCall(
        errorMessage = "Failed to retrieve search history"
    ) {
        local.getQueries(sizeLimit).map {
            it.map { query -> query.toEntity() }
        }
    }

    override suspend fun addSearchHistory(query: String) = safeCall(
        errorMessage = "Failed to add search history",
    ) {
        local.insertQuery(query)
    }


    override suspend fun clearSearchHistory() = safeCall(
        errorMessage = "Failed to clear search history",
    ) {
        local.deleteAllQueries()
    }


    override suspend fun removeSearchHistoryById(id: Int) = safeCall(
        errorMessage = "Failed to remove search history",
    ) {
        local.deleteQueryById(id)
    }


    override suspend fun getRecentViewed(sizeLimit: Int): Flow<List<RecentViewedMedia>> = safeCall(
        errorMessage = "Failed to retrieve recent viewed"
    ) {
        local.getAllRecentViewed(sizeLimit).map {
            it.map { recentViewed -> recentViewed.toEntity() }
        }
    }

    override suspend fun addRecentViewedMedia(item: RecentViewedMedia) = safeCall(
        errorMessage = "Failed to add recent viewed",
    ) {
        local.insertRecentViewed(item.toDto())
    }

    override suspend fun clearRecentViewed() = safeCall(
        errorMessage = "Failed to clear recent viewed",
    ) {
        local.deleteAllRecentViewed()
    }

    override suspend fun addWatchedMediaHistory(
        username: String,
        media: MediaHistoryItem
    ) = safeCall(
        errorMessage = "Failed to add watched media history for user $username",
    ) {
        local.insertWatchedMediaHistory(media.toDto(username))
    }

    override suspend fun getWatchedMediaHistory(
        username: String,
        mediaType: MediaType?,
        genreId: Int?
    ): Flow<List<MediaHistoryItem>> = safeCall(
        errorMessage = "Failed to retrieve watched media history for user $username"
    ) {
        val watchedHistoryDots = local.getWatchedMediaHistory(username, mediaType, genreId)
        watchedHistoryDots.map {
            it.map { watchedHistory -> watchedHistory.toEntity() }
        }
    }

}