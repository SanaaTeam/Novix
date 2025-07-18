package details.repository

import entity.Actor
import entity.Movie
import entity.TvSeries

interface ActorRepository {
    suspend fun getActorDetails(id: Int): Actor
    suspend fun getProfileImages(id: Int, count: Int): List<String>
    suspend fun getGalleryImages(id: Int): List<String>
    suspend fun getActorTopMovies(id: Int): List<Movie>
    suspend fun getActorTopTvSeries(id: Int): List<TvSeries>
}
