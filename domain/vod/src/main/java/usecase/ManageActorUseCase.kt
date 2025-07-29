package usecase

import entity.Actor
import entity.Movie
import entity.TvSeries
import repository.ActorRepository
import javax.inject.Inject

class ManageActorUseCase @Inject constructor(
    private val actorRepo: ActorRepository
) {
    suspend fun getActorDetails(id: Int): Actor =
        actorRepo.getActorDetails(id)

    suspend fun getActorTopMovies(id: Int): List<Movie> =
        actorRepo.getActorTopMovies(id)

    suspend fun getActorTopTvSeries(id: Int): List<TvSeries> =
        actorRepo.getActorTopTvShows(id)

    suspend fun getGalleryImages(id: Int): List<String> =
        actorRepo.getGalleryImageUrls(id)

    suspend fun getProfileImages(id: Int): List<String> =
        actorRepo.getProfileImageUrls(id, IMAGE_COUNT)

    suspend fun getTrendingActors(page: Int): List<Actor> {
        return actorRepo.getTrendingActors(page)
    }

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
