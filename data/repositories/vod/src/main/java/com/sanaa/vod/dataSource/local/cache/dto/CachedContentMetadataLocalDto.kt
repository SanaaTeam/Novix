package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sanaa.vod.util.TimeUtils

@Entity(
    tableName = "cached_content_metadata",
    primaryKeys = ["id", "language", "category"],
)
data class CachedContentMetadataLocalDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = TimeUtils.getCurrentTimeStamp()
){
    enum class Category {
        POPULAR,
        TOP_RATED,
        UPCOMING,
    }
}