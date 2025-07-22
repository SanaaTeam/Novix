package com.sanaa.vod.dataSource.local.search.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sanaa.vod.util.TimeUtils

@Entity(tableName = "recent_viewed")
data class RecentViewedLocalDto(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "is_saved")
    val isSaved: Boolean = false,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = TimeUtils.getCurrentTimeStamp(),
    @ColumnInfo(name = "media_type")
    val mediaType: String
)