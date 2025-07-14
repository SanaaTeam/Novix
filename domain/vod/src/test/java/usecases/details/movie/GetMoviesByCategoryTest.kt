package usecases.details.movie

import details.repository.MovieRepository
import details.usecase.movie.GetMoviesByCategory
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

class GetMoviesByCategoryTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByCategory: GetMoviesByCategory

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getMoviesByCategory = GetMoviesByCategory(movieRepository)
    }
}