package com.sanaa.search.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.data.local.entity.MoviesLocalDto

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MoviesLocalDto)

    @Query("""
        SELECT * FROM movies 
        WHERE (:query IS NULL OR title LIKE '%' || :query || '%')
          AND (:minYear IS NULL OR release_year >= :minYear)
          AND (:maxYear IS NULL OR release_year <= :maxYear)
          AND (:genre IS NULL OR genres LIKE '%' || :genre || '%')
          AND (:rating IS NULL OR imdb_rating >= :rating)
    """)
    suspend fun getFilteredMovies(
        query: String? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        genre: String? = null,
        rating: Float? = null
    ): List<MoviesLocalDto>
}