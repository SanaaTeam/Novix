package home.usecase

import entity.Movie
import home.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetPopularMoviesUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return homeRepository.getPopularMovies()
    }
}
