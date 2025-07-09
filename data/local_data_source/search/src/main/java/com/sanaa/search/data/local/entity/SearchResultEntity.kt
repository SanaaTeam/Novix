package com.sanaa.search.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_results")
data class SearchResultEntity(
    @PrimaryKey val id: Int,
    val query: String,
    val language: String,
    val timestamp: Long,
    val mediaType: String, // movie / tv / actor
    val titleOrName: String,
    val imagePath: String?
) 