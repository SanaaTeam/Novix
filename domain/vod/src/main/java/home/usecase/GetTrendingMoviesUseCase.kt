package home.usecase

import entity.Movie
import home.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetTrendingMoviesUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return homeRepository.getTrendingMovies()
    }
}
