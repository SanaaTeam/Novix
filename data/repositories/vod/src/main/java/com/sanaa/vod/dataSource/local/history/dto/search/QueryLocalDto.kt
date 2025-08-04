package com.sanaa.vod.dataSource.local.history.dto.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sanaa.vod.util.TimeUtils

@Entity(
    tableName = "queries",
    indices = [Index(value = ["search_query"], unique = true)]
)
data class QueryLocalDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "search_query")
    val query: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = TimeUtils.getCurrentTimeStamp()
)