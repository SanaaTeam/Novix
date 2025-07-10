package com.sanaa.search.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searches")
data class SearchLocalDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "search_id")
    val searchId: Long = 0L,

    @ColumnInfo(name = "query")
    val query: String,

    @ColumnInfo(name = "language")
    val language: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
) 