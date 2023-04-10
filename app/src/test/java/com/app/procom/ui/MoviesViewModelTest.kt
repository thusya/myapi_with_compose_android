@file:OptIn(ExperimentalCoroutinesApi::class)

package com.app.procom.ui

import app.cash.turbine.test
import com.app.procom.corountines.MainDispatcherRule
import navigation.NavigationManager
import com.app.procom.ui.home.MoviesUiState
import com.app.procom.ui.home.MoviesViewModel
import com.app.procom.ui.models.MovieDetails
import com.app.procom.usecase.MoviesUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.*
import kotlin.test.assertEquals

class MoviesViewModelTest {

    @get: Rule
    val mainDispatchers = MainDispatcherRule()

    private val mockUseCase: MoviesUseCase = mockk(relaxed = true)
    private val mockNavigationManager: NavigationManager = mockk(relaxed = true)

    lateinit var instance: MoviesViewModel

    @BeforeEach
    fun beforeEach() {
        instance = spyk(MoviesViewModel(mockUseCase, mockNavigationManager))
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }

    @DisplayName("When load the movies")
    @Nested
    inner class LoadMovies {
        @DisplayName("Then try to emit empty state if no movies fetched")
        @Test
        fun test() = runTest {

            val mockList = emptyList<MovieDetails>()
            coEvery { mockUseCase.fetchAll() } returns mockList

            instance.loadMovies()

            coVerify { mockUseCase.fetchAll() }
            instance.listingUiState.test {
                assert(awaitItem() is MoviesUiState.Empty)
            }
        }

        @DisplayName("Then try to emit DisplayMovies when movies fetched")
        @Test
        fun test1() = runTest {

            val mockList = listOf(MovieDetails(), MovieDetails())
            coEvery { mockUseCase.fetchAll() } returns mockList

            instance.loadMovies()

            coVerify { mockUseCase.fetchAll() }
            instance.listingUiState.test {
                val item = awaitItem()
                assert(item is MoviesUiState.DisplayMovies)
                assertEquals(mockList.size, (item as MoviesUiState.DisplayMovies).data.size)
            }
        }
    }

    @DisplayName("When load the favorite movies")
    @Nested
    inner class LoadFavoriteMovies {
        @DisplayName("Then try to emit empty state if no favorites movies fetched")
        @Test
        fun test() = runTest {

            val mockList = emptyList<MovieDetails>()
            coEvery { mockUseCase.fetchFavoriteMovies() } returns mockList

            instance.loadFavoriteMovies()

            coVerify { mockUseCase.fetchFavoriteMovies() }
            instance.listingUiState.test {
                assert(awaitItem() is MoviesUiState.Empty)
            }
        }

        @DisplayName("Then try to emit  when movies fetched")
        @Test
        fun test1() = runTest {

            val mockList = listOf(MovieDetails(), MovieDetails())
            coEvery { mockUseCase.fetchFavoriteMovies() } returns mockList

            instance.loadFavoriteMovies()

            coVerify { mockUseCase.fetchFavoriteMovies() }
            instance.listingUiState.test {
                val item = awaitItem()
                assert(item is MoviesUiState.DisplayMovies)
                assertEquals(mockList.size, (item as MoviesUiState.DisplayMovies).data.size)
            }
        }
    }

    @DisplayName("When fetch the favorite by Id")
    @Nested
    inner class FetchMovieById {
        @DisplayName("Then try to emit the movie details")
        @Test
        fun test() = runTest {

            val mockMovieDetail = MovieDetails()
            val id = 1

            coEvery { mockUseCase.fetchMovieById(any()) } returns mockMovieDetail

            instance.fetchMovieById(id)

            coVerify { mockUseCase.fetchMovieById(id) }

            assertEquals(
                instance.listingUiState.value::class,
                MoviesUiState.DisplayMovieDetails::class
            )

            instance.listingUiState.test {
                val item = awaitItem()
                assert((item as MoviesUiState.DisplayMovieDetails).data == mockMovieDetail)
            }
        }
    }

    @DisplayName("When bookmark movie by Id")
    @Nested
    inner class BookmarkFavoriteMovie {
        @DisplayName("Then try to load the movie details")
        @Test
        fun test() = runTest {
            val id = 2
            val bookmarked = true

            coEvery { mockUseCase.updateFavoriteMovie(any(), any()) } returns 1

            instance.bookmarkFavoriteMovie(id, bookmarked)

            coVerify { mockUseCase.updateFavoriteMovie(id, bookmarked) }
            coVerify { mockUseCase.fetchAll() }
        }

        @DisplayName("Then try to emit the error details")
        @Test
        fun test1() = runTest {
            val id = 2
            val bookmarked = true

            coEvery { mockUseCase.updateFavoriteMovie(any(), any()) } returns 0

            instance.bookmarkFavoriteMovie(id, bookmarked)

            instance.listingUiState.test {
                assertEquals(MoviesUiState.Error::class, awaitItem()::class, )
            }
        }
    }
}
