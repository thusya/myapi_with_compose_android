package com.app.procom.respository

import com.app.procom.models.movies.Movies
import com.app.procom.models.movies.Next
import com.app.procom.repository.movies.remote.MoviesRemoteDataSource
import com.app.procom.repository.movies.remote.MoviesRemoteDataSourceImpl
import com.app.procom.services.MoviesApi
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import com.app.procom.models.movies.MovieDetails as RemoteModels

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesRemoteDataSourceTest {

    private val mockMovieApi: MoviesApi = mockk(relaxed = true)
    lateinit var instance: MoviesRemoteDataSource

    @BeforeEach
    fun beforeEach() {
        instance = MoviesRemoteDataSourceImpl(mockMovieApi)
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }

    @DisplayName("When fetch Movie by id")
    @Nested
    inner class FetchMovieById {
        @DisplayName("Then fetch the movie details by using movies api")
        @Test
        fun test() = runTest {
            val id = 2
            val movieDetails = RemoteModels()
            coEvery { mockMovieApi.getMovieById(any()) } returns movieDetails

            val result = instance.fetchMovieById(id)

            coVerify { mockMovieApi.getMovieById(id) }
            assertEquals(movieDetails, result)

        }
    }

    @DisplayName("When fetch Movies")
    @Nested
    inner class FetchMovies {
        @DisplayName("Then fetch the movies by using movies api")
        @Test
        fun test() = runTest {
            val movieList = listOf(RemoteModels(), RemoteModels())
            val movies = Movies(Next(), movieList)
            coEvery { mockMovieApi.getMovies(any()) } returns movies

            val result = instance.fetchMovies()

            coVerify { mockMovieApi.getMovies(page = 0) }
            assertEquals(movies.results.size, result.results.size)

        }
    }
}