package com.app.procom.services

import com.app.procom.models.movies.MovieDetails
import com.app.procom.models.movies.Movies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("api/movies")
    suspend fun getMovies(@Query("page") page: Int): Movies

    @GET("api/movies/{id}")
    suspend fun getMovieById(@Path("id") id: Int): MovieDetails
}