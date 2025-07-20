package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.QueryLocalDto
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import search.usecase.search_param.SearchHistory

class MapSearchHistoryTest {

    @Test
    fun `toEntity maps QueryLocalDto to SearchHistory`() {
        val queryLocalDto = QueryLocalDto(
            id = 1,
            query = "query",
            timestamp = 1234567890L
        )
        val expectedSearchHistory = SearchHistory(
            id = 1,
            query = "query",
            timestamp = Instant.fromEpochMilliseconds(1234567890L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
        assertEquals(expectedSearchHistory, queryLocalDto.toEntity())
    }
}