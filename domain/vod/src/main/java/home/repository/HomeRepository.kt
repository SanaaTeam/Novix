package home.repository

import entity.Actor
import entity.Movie
import entity.TvSeries
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getPopularMovies(): Flow<List<Movie>>

    fun getTopRatedMovies(): Flow<List<Movie>>

    fun getTopRatedTvSeries(): Flow<List<TvSeries>>

    fun getUpcomingMovies(): Flow<List<Movie>>

    fun getTrendingMovies(): Flow<List<Movie>>

    fun getTrendingTvSeries(): Flow<List<TvSeries>>

    fun getTrendingPeople(): Flow<List<Actor>>
}
