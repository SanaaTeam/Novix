package usecases.details.movie

import details.repository.MovieRepository
import details.usecase.movie.GetReviewsByMovieId
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

class GetReviewsByMovieIdTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getReviewsByMovieId: GetReviewsByMovieId

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getReviewsByMovieId = GetReviewsByMovieId(movieRepository)
    }
}