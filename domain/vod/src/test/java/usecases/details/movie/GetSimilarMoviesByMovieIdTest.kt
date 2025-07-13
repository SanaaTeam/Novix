package usecases.details.movie

import details.repository.MovieRepository
import details.usecase.movie.GetSimilarMoviesByMovieId
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

class GetSimilarMoviesByMovieIdTest(){
    private lateinit var movieRepository: MovieRepository
    private lateinit var getSimilarMoviesByMovieId: GetSimilarMoviesByMovieId

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getSimilarMoviesByMovieId = GetSimilarMoviesByMovieId(movieRepository)
    }
}