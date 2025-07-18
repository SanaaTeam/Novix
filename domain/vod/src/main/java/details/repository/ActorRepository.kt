package details.repository

import entity.Actor
import entity.Movie
import entity.TvSeries

interface ActorRepository {
    suspend fun getActorDetails(actorId: Int): Actor
    suspend fun getProfileImages(actorId: Int): List<String>
    suspend fun getGalleryImages(actorId: Int): List<String>
    suspend fun getActorTopMovies(actorId: Int): List<Movie>
    suspend fun getActorTopTvSeries(actorId: Int): List<TvSeries>
}
