package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "saved_list_item",
    primaryKeys = ["list_id", "movie_id"]
)
data class SavedListItemLocalDto(
    @ColumnInfo(name = "list_id")
    val listId: Int,
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "added_at")
    val addedAt: Long = System.currentTimeMillis()
)