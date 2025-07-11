package com.sanaa.search.dataSource.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "actor", primaryKeys = ["id", "language"],
)
data class ActorsLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image_path")
    val imagePath: String?,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
) 