package com.sanaa.vod.dataSource.local.continueWatch

import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto
import kotlinx.coroutines.flow.Flow

interface LocalContinueWatchingDataSource {
    suspend fun addOrUpdateMedia(continueWatchingDto: ContinueWatchingLocalDto)
    fun getContinueWatchingList(username: String, limit: Int): Flow<List<ContinueWatchingLocalDto>>
}