package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cached_content_metadata",
    indices = [Index(value = ["category"], unique = true)
    ]
)
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
        POPULAR_MEDIA,
        TOP_RATED_MEDIA,
        UPCOMING_MEDIA,
        MOVIE_GENRES,
        TV_SHOW_GENRES,
    }
}