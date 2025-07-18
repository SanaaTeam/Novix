package details.usecase

import details.repository.ActorRepository
import entity.Actor
import entity.Movie
import entity.TvSeries

class ManageActorDetailsUseCase(
    private val actorRepo: ActorRepository
) {
    suspend fun getActorDetails(id: Int): Actor =
        actorRepo.getActorDetails(id)

    suspend fun getActorTopMovies(id: Int): List<Movie> =
        actorRepo.getActorTopMovies(id)

    suspend fun getActorTopTvSeries(id: Int): List<TvSeries> =
        actorRepo.getActorTopTvSeries(id)

    suspend fun getGalleryImages(id: Int): List<String> =
        actorRepo.getGalleryImages(id)

    suspend fun getProfileImages(id: Int): List<String> =
        actorRepo.getProfileImages(id)
}
