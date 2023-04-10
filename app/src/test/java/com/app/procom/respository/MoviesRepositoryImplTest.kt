package com.app.procom.respository

import com.app.procom.mappers.MovieDataMapper
import com.app.procom.models.movies.MovieDetails
import com.app.procom.models.movies.Movies
import com.app.procom.models.movies.Next
import com.app.procom.repository.movies.MoviesRepository
import com.app.procom.repository.movies.MoviesRepositoryDependencies
import com.app.procom.repository.movies.MoviesRepositoryImpl
import com.app.procom.repository.movies.local.MovieLocalDataSource
import com.app.procom.repository.movies.remote.MoviesRemoteDataSource
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import com.app.procom.storage.movies.MovieDetails as DbMovieDetails


@OptIn(ExperimentalCoroutinesApi::class)
class MoviesRepositoryImplTest {

    private val localDataSource: MovieLocalDataSource = mockk(relaxed = true)
    private val remoteDataSource: MoviesRemoteDataSource = mockk(relaxed = true)
    private val dataMapper: MovieDataMapper = mockk(relaxed = true)

    private lateinit var mockRepositoryDependencies: MoviesRepositoryDependencies
    private lateinit var repository: MoviesRepository


    @BeforeEach
    fun beforeEach() {

        coEvery { localDataSource.insertMovies(any()) } returns Unit
        coEvery { localDataSource.updateFavoriteMovie(any(), any()) } returns 1

        mockRepositoryDependencies =
            MoviesRepositoryDependencies(localDataSource, remoteDataSource, dataMapper)

        repository = MoviesRepositoryImpl(mockRepositoryDependencies)
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }

    @DisplayName("When fetch Movie by id")
    @Nested
    inner class FetchMovieById {
        @DisplayName("Then fetch the movie by remote ds and  store in local ds")
        @Test
        fun test() = runTest {
            val id = 1
            val movieDetails = MovieDetails(id = 1, long_disc = "longDesc", short_desc = "shotDesc")

            coEvery { remoteDataSource.fetchMovieById(any()) } returns movieDetails
            coEvery { dataMapper.map(any<MovieDetails>()) } returns DbMovieDetails()

            val result = repository.fetchById(id)

            coVerify { remoteDataSource.fetchMovieById(id) }
            coVerify { dataMapper.map(any<MovieDetails>()) }
            coVerify { localDataSource.insertMovies(any()) }

            assertEquals(DbMovieDetails::class, result::class)

        }
    }

    @DisplayName("When fetch Movies")
    @Nested
    inner class FetchMovies {
        @DisplayName("Then fetch the movies by using remote ds and store in local ds")
        @Test
        fun test() = runTest {
            val movieList = listOf(MovieDetails(), MovieDetails())
            val movies = Movies(next = Next(), movieList)

            coEvery { remoteDataSource.fetchMovies() } returns movies
            coEvery { dataMapper.map(any<MovieDetails>()) } returns DbMovieDetails()

            val result = repository.fetchMovies()

            coVerify { remoteDataSource.fetchMovies() }
            coVerify { dataMapper.map(any<MovieDetails>()) }
            assertEquals(movieList.size, result.size)
        }
    }

    @DisplayName("When update the favorite movie by id")
    @Nested
    inner class UpdateFavoriteMovie {
        @DisplayName("Then update favorite in local ds")
        @Test
        fun test1() = runTest {
            val id = 1
            val bookMarked = true

            coEvery { localDataSource.updateFavoriteMovie(any(), any()) } returns 1

            val result = repository.updateFavoriteMovie(id, bookMarked)

            coVerify { localDataSource.updateFavoriteMovie(id, bookMarked) }
            assertEquals(1, result)
        }
    }
}