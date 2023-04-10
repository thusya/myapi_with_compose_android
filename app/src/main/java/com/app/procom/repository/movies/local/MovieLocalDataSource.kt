package com.app.procom.repository.movies.local

import com.app.procom.storage.movies.MovieDetails
import com.app.procom.storage.movies.MoviesDao

interface MovieLocalDataSource {
    suspend fun fetchMovieById(id: Int): MovieDetails
    suspend fun fetchMovies(): List<MovieDetails>
    suspend fun updateFavoriteMovie(id: Int, isBookmarked: Boolean): Int
    suspend fun insertMovies(movies: List<MovieDetails>)
}

class MovieLocalDataSourceImpl(private val dao: MoviesDao) : MovieLocalDataSource {
    override suspend fun fetchMovieById(id: Int): MovieDetails {
        return dao.fetchMovieById(id)
    }

    override suspend fun fetchMovies(): List<MovieDetails> {
        return dao.fetchMovies()
    }

    override suspend fun updateFavoriteMovie(id: Int, isBookmarked: Boolean): Int {
        val bookMarked = if (isBookmarked) 1 else 0
        return dao.updateFavorite(id, bookMarked)
    }

    override suspend fun insertMovies(movies: List<MovieDetails>) {
        dao.insertMovies(movies)
    }
}