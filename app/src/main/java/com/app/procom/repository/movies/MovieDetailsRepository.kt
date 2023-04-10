package com.app.procom.repository.movies

import com.app.procom.mappers.MovieDataMapper
import com.app.procom.repository.movies.local.MovieLocalDataSource
import com.app.procom.repository.movies.remote.MoviesRemoteDataSource
import com.app.procom.storage.movies.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun fetchMovies(): List<MovieDetails>
    suspend fun fetchById(id: Int): MovieDetails
    suspend fun updateFavoriteMovie(id: Int, isBookmarked: Boolean): Int
}

class MoviesRepositoryDependencies(
    val localDataSource: MovieLocalDataSource,
    val remoteDataSource: MoviesRemoteDataSource,
    val dataMapper: MovieDataMapper
)

class MoviesRepositoryImpl(private val dependencies: MoviesRepositoryDependencies) :
    MoviesRepository {
    override suspend fun fetchMovies(): List<MovieDetails> {
        val movies = dependencies.remoteDataSource.fetchMovies()
        val data = movies.results.map { dependencies.dataMapper.map(it) }
        dependencies.localDataSource.insertMovies(data)
        return data
    }

    override suspend fun fetchById(id: Int): MovieDetails {
        val remoteMovie = dependencies.remoteDataSource.fetchMovieById(id)
        val data = dependencies.dataMapper.map(remoteMovie)
        dependencies.localDataSource.insertMovies(listOf(data))
        return data
    }

    override suspend fun updateFavoriteMovie(id: Int, isBookmarked: Boolean): Int {
        return dependencies.localDataSource.updateFavoriteMovie(id, isBookmarked)
    }
}