package com.app.procom.repository.movies.remote

import com.app.procom.models.movies.MovieDetails
import com.app.procom.models.movies.Movies
import com.app.procom.services.MoviesApi

interface MoviesRemoteDataSource {
    suspend fun fetchMovies(): Movies
    suspend fun fetchMovieById(id: Int): MovieDetails
}

class MoviesRemoteDataSourceImpl(private val moviesApi: MoviesApi): MoviesRemoteDataSource {
    override suspend fun fetchMovies(): Movies {
        return moviesApi.getMovies(page = 0)
    }

    override suspend fun fetchMovieById(id: Int): MovieDetails {
        return moviesApi.getMovieById(id)
    }
}