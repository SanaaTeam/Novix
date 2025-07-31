package com.sanaa.vod.dataSource.local.continueWatch

import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto

interface LocalContinueWatchingDataSource {
    suspend fun addOrUpdateItem(item: ContinueWatchingLocalDto)
    suspend fun getContinueWatchingList(username: String, limit: Int): List<ContinueWatchingLocalDto>
    suspend fun deleteItem(mediaId: Int, username: String)
}