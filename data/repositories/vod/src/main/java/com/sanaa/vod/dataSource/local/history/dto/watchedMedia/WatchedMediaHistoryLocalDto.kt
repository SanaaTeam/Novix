package com.sanaa.vod.dataSource.local.history.dto.watchedMedia

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.sanaa.vod.util.TimeUtils

@Entity(tableName = "watched_media_history", primaryKeys = ["id", "username"])
data class WatchedMediaHistoryLocalDto(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "poster_image_url")
    val posterImageUrl: String,
    @ColumnInfo(name = "media_type")
    val mediaType: String,
    @ColumnInfo(name = "genres")
    val genres: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = TimeUtils.getCurrentTimeStamp()
)