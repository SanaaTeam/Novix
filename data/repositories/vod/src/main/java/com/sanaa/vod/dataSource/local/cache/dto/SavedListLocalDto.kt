package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "saved_list")
data class SavedListLocalDto(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "item_count") val itemCount: Int
)