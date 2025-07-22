package home.usecase

import entity.Movie
import home.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetUpcomingMoviesUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return homeRepository.getUpcomingMovies()
    }
}
