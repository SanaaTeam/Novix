package com.sanaa.vod.continueWatch

import com.sanaa.vod.continueWatch.dao.ContinueWatchingDao
import com.sanaa.vod.dataSource.local.continueWatch.LocalContinueWatchingDataSource
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto

class LocalContinueWatchingDataSourceImpl(
    private val continueWatchingDao: ContinueWatchingDao
) : LocalContinueWatchingDataSource {

    override suspend fun addOrUpdateItem(item: ContinueWatchingLocalDto) {
        continueWatchingDao.insertOrUpdate(item)
    }

    override suspend fun getContinueWatchingList(username: String, limit: Int): List<ContinueWatchingLocalDto> {
        return continueWatchingDao.getContinueWatchingList(username, limit)
    }

    override suspend fun deleteItem(mediaId: Int, username: String) {
        continueWatchingDao.deleteItem(mediaId, username)
    }
}