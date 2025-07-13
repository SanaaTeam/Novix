package details.repository

import entity.Actor
import entity.Movie
import entity.TvSeries

interface ActorRepository {
    suspend fun getActorDetails(id: Int): Actor
    suspend fun getProfileImages(id: Int): List<String>
    suspend fun getGalleryImages(id: Int): List<String>
    suspend fun getTopMovies(id: Int): List<Movie>
    suspend fun getTopTvSeries(id: Int): List<TvSeries>
}
