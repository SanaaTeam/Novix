package com.sanaa.search.dataSource.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "searches",
    indices = [
        Index(
            value = ["query", "language"],
            unique = true
        ),
        Index(
            value = ["id"],
            unique = true
        )
    ]
)
data class SearchLocalDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "query")
    val query: String,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)