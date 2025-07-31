package com.sanaa.vod.dataSource.local.continueWatch.dto

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "continue_watching",
    primaryKeys = ["media_id", "username"]
)
data class ContinueWatchingLocalDto(
    @ColumnInfo(name = "media_id")
    val mediaId: Int,

    @ColumnInfo(name = "episode_id")
    val episodeId: Int?,

    @ColumnInfo(name = "media_type")
    val mediaType: String,

    @ColumnInfo(name = "username")
    val username: String
)