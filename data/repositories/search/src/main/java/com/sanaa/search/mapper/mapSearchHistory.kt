package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.QueryLocalDto
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import usecase.search.SearchHistory


fun QueryLocalDto.toEntity(): SearchHistory {
    return SearchHistory(
        id = id,
        query = query,
        timestamp = Instant.fromEpochMilliseconds(this.timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    )
}
