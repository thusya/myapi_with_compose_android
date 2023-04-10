package com.app.procom.respository

import com.app.procom.repository.movies.local.MovieLocalDataSource
import com.app.procom.repository.movies.local.MovieLocalDataSourceImpl
import com.app.procom.storage.movies.MovieDetails
import com.app.procom.storage.movies.MoviesDao
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesLocalDataSourceTest {
    private val mockDao: MoviesDao = mockk(relaxed = true)
    lateinit var instance: MovieLocalDataSource

    @BeforeEach
    fun beforeEach() {
        instance = MovieLocalDataSourceImpl(mockDao)
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }


    @DisplayName("When fetch Movie by id")
    @Nested
    inner class FetchMovieById {
        @DisplayName("Then fetch the movie details by using dao")
        @Test
        fun test() = runTest {
            val id = 2
            val movieDetails = MovieDetails()
            coEvery { mockDao.fetchMovieById(any()) } returns movieDetails

            val result = instance.fetchMovieById(2)

            coVerify { mockDao.fetchMovieById(id) }
            assertEquals(movieDetails, result)

        }
    }

    @DisplayName("When fetch Movies")
    @Nested
    inner class FetchMovies {
        @DisplayName("Then fetch the movies by using dao")
        @Test
        fun test() = runTest {
            val movies = listOf(MovieDetails(), MovieDetails())
            coEvery { mockDao.fetchMovies() } returns movies

            val result = instance.fetchMovies()

            coVerify { mockDao.fetchMovies() }
            assertEquals(movies.size, result.size)
        }
    }

    @DisplayName("When update the favorite movie by id")
    @Nested
    inner class UpdateFavoriteMovie {
        @DisplayName("Then update favorite by using dao")
        @Test
        fun test1() = runTest {
            val id = 3
            val bookmarked = true

            coEvery { mockDao.updateFavorite(any(), any()) } returns 1

            val result = instance.updateFavoriteMovie(id, bookmarked)

            coVerify { mockDao.updateFavorite(id, 1) }
            assertEquals(1, result)
        }

        @DisplayName("Then don't update favorite by using dao")
        @Test
        fun test2() = runTest {
            val id = 3
            val bookmarked = false

            coEvery { mockDao.updateFavorite(any(), any()) } returns 0

            val result = instance.updateFavoriteMovie(id, bookmarked)

            coVerify { mockDao.updateFavorite(id, 0) }
            assertEquals(0, result)
        }
    }

    @DisplayName("When insert Movies as list")
    @Nested
    inner class InsertMovies {
        @DisplayName("Then insert movies by using dao")
        @Test
        fun test() = runTest {
            val movies = listOf(MovieDetails(), MovieDetails())

            instance.insertMovies(movies)

            coVerify { mockDao.insertMovies(movies) }

        }
    }
}