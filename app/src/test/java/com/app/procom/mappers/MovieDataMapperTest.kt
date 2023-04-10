package com.app.procom.mappers

import com.app.procom.models.movies.MovieDetails as RemoteMovieDetails
import com.app.procom.storage.movies.MovieDetails as LocalMovieDetails
import com.app.procom.ui.models.MovieDetails as UiMovieDetails
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieDataMapperTest {

    private val mockRemoteData = mockk<RemoteMovieDetails>()
    private val mockLocalDbMovies = mockk<LocalMovieDetails>()

    @Test
    fun `map should map remote data to local DB data`() {
        every { mockRemoteData.id } returns 123
        every { mockRemoteData.images } returns listOf("url1", "url2")
        every { mockRemoteData.long_disc } returns "long description"
        every { mockRemoteData.short_desc } returns "short description"
        every { mockRemoteData.title } returns "movie title"
        every { mockRemoteData.date } returns "2022-03-11"
        val expectedLocalDbMovies = LocalMovieDetails(
            movieId = 123,
            imageUrls = listOf("url1", "url2"),
            longDescription = "long description",
            shortDescription = "short description",
            title = "movie title",
            createdDate = "2022-03-11"
        )
        val actualLocalDbMovies = MovieDataMapper().map(mockRemoteData)

        assertEquals(expectedLocalDbMovies.createdDate, actualLocalDbMovies.createdDate)
        assertEquals(expectedLocalDbMovies.id, actualLocalDbMovies.id)
        assertEquals(expectedLocalDbMovies.imageUrls, actualLocalDbMovies.imageUrls)
        assertEquals(expectedLocalDbMovies.longDescription, actualLocalDbMovies.longDescription)
        assertEquals(expectedLocalDbMovies.shortDescription, actualLocalDbMovies.shortDescription)
        assertEquals(expectedLocalDbMovies.title, actualLocalDbMovies.title)
    }

    @Test
    fun `map should map local DB data to UI model`() {
        every { mockLocalDbMovies.id } returns 456
        every { mockLocalDbMovies.imageUrls } returns listOf("url3", "url4")
        every { mockLocalDbMovies.longDescription } returns "long description"
        every { mockLocalDbMovies.shortDescription } returns "short description"
        every { mockLocalDbMovies.title } returns "movie title"
        every { mockLocalDbMovies.createdDate } returns "2022-03-11"
        every { mockLocalDbMovies.isFavorite } returns 1
        val expectedUiModelMovies = UiMovieDetails(
            date = "2022-03-11",
            id = 456,
            images = "[url3, url4]",
            longDescription = "long description",
            shortDescription = "short description",
            isBookmarked = true
        )

        val actualUiModelMovies = MovieDataMapper().map(mockLocalDbMovies)

        assertEquals(expectedUiModelMovies.id, actualUiModelMovies.id)
        assertEquals(expectedUiModelMovies.date, actualUiModelMovies.date)
        assertEquals(expectedUiModelMovies.images, actualUiModelMovies.images)
        assertEquals(expectedUiModelMovies.longDescription, actualUiModelMovies.longDescription)
        assertEquals(expectedUiModelMovies.isBookmarked, actualUiModelMovies.isBookmarked)
    }
}
