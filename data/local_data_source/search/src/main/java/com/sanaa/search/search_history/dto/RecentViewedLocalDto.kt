package com.sanaa.search.search_history.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_viewed")
data class RecentViewedLocalDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "is_saved")
    val isSaved: Boolean = false,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)