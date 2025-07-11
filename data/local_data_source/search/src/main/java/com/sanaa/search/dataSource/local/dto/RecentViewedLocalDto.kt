package com.sanaa.search.dataSource.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_viewed")
data class RecentViewedLocalDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "item_id")
    val itemId: Int,
    @ColumnInfo(name = "item_type")
    val itemType: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
) 