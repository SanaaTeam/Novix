package com.sanaa.search.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "search_result",
    primaryKeys = ["search_id", "item_id", "item_type"],
    foreignKeys = [
        ForeignKey(
            entity = SearchLocalDto::class,
            parentColumns = ["search_id"],
            childColumns = ["search_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SearchResultLocalDto(
    @ColumnInfo(name = "search_id")
    val searchId: Long,

    @ColumnInfo(name = "item_id")
    val itemId: Int,

    @ColumnInfo(name = "item_type")
    val itemType: String
)