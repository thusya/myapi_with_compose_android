package com.app.procom.usecase

import com.app.procom.mappers.MovieDataMapper
import com.app.procom.repository.movies.MoviesRepository
import com.app.procom.storage.movies.MovieDetails
import com.app.procom.ui.models.MovieDetails as UiMovieDetails
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MovieUseCaseTest {

    val mockRepository: MoviesRepository = mockk(relaxed = true)
    val mockDataMapper: MovieDataMapper = mockk(relaxed = true)

    lateinit var instance: MoviesUseCase

    @BeforeEach
    fun beforeEach() {
        instance = spyk(MoviesUseCase(mockRepository, mockDataMapper))
        every { mockDataMapper.map(any<MovieDetails>()) } returns UiMovieDetails()
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }


    @DisplayName("When fetch the favorite movies")
    @Nested
    inner class FetchFavoriteMovies {
        @DisplayName("Then fetch favorite movies from repository")
        @Test
        fun test() = runTest {

            val mockList = listOf(MovieDetails(), MovieDetails(isFavorite = 1))

            coEvery { mockRepository.fetchMovies() } returns mockList

            val result = instance.fetchFavoriteMovies()

            assertEquals(1, result.size)

        }
    }

    @DisplayName("When fetch all movies")
    @Nested
    inner class FetchAll {
        @DisplayName("Then fetch all movies from repository")
        @Test
        fun test() = runTest {
            val mockList = listOf(MovieDetails(), MovieDetails(isFavorite = 1))

            coEvery { mockRepository.fetchMovies() } returns mockList

            val result = instance.fetchAll()

            assertEquals(mockList.size, result.size)
        }
    }

    @DisplayName("When fetch movie by id")
    @Nested
    inner class FetchMovieById {
        @DisplayName("Then fetch movie details from repository")
        @Test
        fun test() = runTest {
            val id = 1
            val mockMovieDetails = MovieDetails()

            coEvery { mockRepository.fetchById(any()) } returns mockMovieDetails

            instance.fetchMovieById(id)

            coVerify { mockRepository.fetchById(id) }
            verify { mockDataMapper.map(mockMovieDetails) }

        }
    }

    @DisplayName("When update favorite movie")
    @Nested
    inner class UpdateFavoriteMovie {
        @DisplayName("Then update the movie on repository")
        @Test
        fun test() = runTest {
            val id = 1
            val isBookmarked = true

            coEvery { mockRepository.updateFavoriteMovie(any(), any()) } returns 0

            val result = instance.updateFavoriteMovie(id, isBookmarked)

            coVerify { mockRepository.updateFavoriteMovie(id, isBookmarked) }
            assertEquals(0, result)
        }
    }
}