package usecase

import entity.Actor
import entity.Movie
import entity.TvShow
import repository.ActorRepository
import javax.inject.Inject

class ManageActorUseCase @Inject constructor(
    private val actorRepo: ActorRepository
) {
    suspend fun getActorDetails(id: Int): Actor =
        actorRepo.getActorDetails(id)

    suspend fun getActorTopMovies(id: Int): List<Movie> =
        actorRepo.getActorTopMovies(id)

    suspend fun getActorTopTvSeries(id: Int): List<TvShow> =
        actorRepo.getActorTopTvShows(id)

    suspend fun getGalleryImages(id: Int): List<String> =
        actorRepo.getGalleryImageUrls(id)

    suspend fun getProfileImages(id: Int, count: Int = IMAGE_COUNT): List<String> =
        actorRepo.getProfileImageUrls(id, count)

    suspend fun getTrendingActors(page: Int): List<Actor> {
        return actorRepo.getTrendingActors(page)
    }

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
