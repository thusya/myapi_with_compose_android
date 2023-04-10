package com.app.procom.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.procom.storage.AppDatabase
import com.app.procom.storage.movies.MovieDetails
import com.app.procom.storage.movies.MoviesDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    private lateinit var movieDao: MoviesDao

    @BeforeEach
    fun beforeEach() {
        val database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        movieDao = database.movieDao()

    }

    @Test
    fun fetchMovies() = runTest {
        val list = generateMockData()
        movieDao.insertMovies(list)

        val result = movieDao.fetchMovies()

        assertEquals(list.size, result.size)
    }

    @Test
    fun fetchMovieById() = runTest {
        val id = 1234
        val list = generateMockData()
        movieDao.insertMovies(list)

        val result = movieDao.fetchMovieById(id)

        val expectedResult = list.find { it.id == id }

        assertEquals(expectedResult?.shortDescription, result.shortDescription)
        assertEquals(expectedResult?.longDescription, result.longDescription)
        assertEquals(expectedResult?.title, result.title)
        assertEquals(expectedResult?.isFavorite, result.isFavorite)
        assertEquals(expectedResult?.createdDate, result.createdDate)

    }

    @Test
    fun updateFavorite() = runTest {
        val id = 91011
        val list = generateMockData()
        movieDao.insertMovies(list)

        movieDao.updateFavorite(id, 0)

        val result = movieDao.fetchMovieById(id)

        val expectedResult = list.find { it.id == id }

        assertEquals(expectedResult?.title, result.title)
        assertEquals(expectedResult?.isFavorite, result.isFavorite)

    }


    private fun generateMockData(): List<MovieDetails> {
        return listOf(
            MovieDetails(
                id = 1,
                movieId = 1234,
                imageUrls = listOf("https://example.com/image1.jpg"),
                longDescription = "This is a long description 1",
                shortDescription = "Short description 1",
                title = "Movie title 1",
                isFavorite = 1,
                createdDate = "2022-03-11"
            ),
            MovieDetails(
                id = 2,
                movieId = 5678,
                imageUrls = listOf("https://example.com/image2.jpg"),
                longDescription = "This is a long description 2",
                shortDescription = "Short description 2",
                title = "Movie title 2",
                isFavorite = 0,
                createdDate = "2022-03-12"
            ),
            MovieDetails(
                id = 3,
                movieId = 91011,
                imageUrls = listOf("https://example.com/image3.jpg"),
                longDescription = "This is a long description 3",
                shortDescription = "Short description 3",
                title = "Movie title 3",
                isFavorite = 1,
                createdDate = "2022-03-13"
            )
        )

    }
}