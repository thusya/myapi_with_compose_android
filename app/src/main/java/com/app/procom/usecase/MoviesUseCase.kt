package com.app.procom.usecase

import com.app.procom.mappers.MovieDataMapper
import com.app.procom.repository.movies.MoviesRepository
import com.app.procom.ui.models.MovieDetails

class MoviesUseCase(
    private val repository: MoviesRepository,
    private val dataMapper: MovieDataMapper
) {

    suspend fun fetchFavoriteMovies(): List<MovieDetails> {
        return repository.fetchMovies().filter { it.isFavorite == 1 }.map { dataMapper.map(it) }
    }

    suspend fun fetchAll(): List<MovieDetails> {
        return repository.fetchMovies().map { dataMapper.map(it) }
    }

    suspend fun fetchMovieById(id: Int): MovieDetails {
        return dataMapper.map(repository.fetchById(id))
    }

    suspend fun updateFavoriteMovie(movieId: Int, isBookmarked: Boolean): Int {
        return repository.updateFavoriteMovie(movieId, isBookmarked)
    }
}
