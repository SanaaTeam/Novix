package com.sanaa.vod.continueWatch

import com.sanaa.vod.continueWatch.dao.ContinueWatchingDao
import com.sanaa.vod.dataSource.local.continueWatch.LocalContinueWatchingDataSource
import com.sanaa.vod.dataSource.local.continueWatch.dto.ContinueWatchingLocalDto
import kotlinx.coroutines.flow.Flow

class LocalContinueWatchingDataSourceImpl(
    private val continueWatchingDao: ContinueWatchingDao
) : LocalContinueWatchingDataSource {

    override suspend fun addOrUpdateItem(item: ContinueWatchingLocalDto) {
        continueWatchingDao.insertOrUpdate(item)
    }

    override fun getContinueWatchingList(username: String, limit: Int): Flow<List<ContinueWatchingLocalDto>> {
        return continueWatchingDao.getContinueWatchingList(username, limit)
    }
}