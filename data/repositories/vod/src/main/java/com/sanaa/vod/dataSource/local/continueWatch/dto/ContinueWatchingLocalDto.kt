package com.sanaa.vod.dataSource.local.continueWatch.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "continue_watching")
data class ContinueWatchingLocalDto(
    @PrimaryKey val mediaId: Int,
    val episodeId: Int?,
    val mediaType: String
)