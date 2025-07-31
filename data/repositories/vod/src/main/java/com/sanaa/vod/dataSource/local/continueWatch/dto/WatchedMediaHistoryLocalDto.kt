package com.sanaa.vod.dataSource.local.continueWatch.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sanaa.vod.util.TimeUtils

@Entity(tableName = "watched_media_history")
data class WatchedMediaHistoryLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "poster_image_url")
    val posterImageUrl: String,

    @ColumnInfo(name = "media_type")
    val mediaType: String,

    @ColumnInfo(name = "genres")
    val genres: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = TimeUtils.getCurrentTimeStamp()
)