package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_content_metadata")
data class CachedContentMetadataLocalDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
) {
    enum class Category {
        POPULAR,
        TOP_RATED,
        UPCOMING,
    }
}