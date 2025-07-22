package details.repository

import entity.Actor
import entity.Movie
import entity.TvSeries

interface ActorRepository {
    suspend fun getActorDetails(id: Int): Actor
    suspend fun getProfileImageUrls(id: Int, count: Int): List<String>
    suspend fun getGalleryImageUrls(id: Int): List<String>
    suspend fun getActorTopMovies(id: Int): List<Movie>
    suspend fun getActorTopTvShows(id: Int): List<TvSeries>
}
