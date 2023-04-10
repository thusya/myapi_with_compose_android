package com.app.procom.storage.movies

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * FROM MovieDetails")
    fun fetchMovies(): List<MovieDetails>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(movies: List<MovieDetails>)

    @Query("SELECT * FROM MovieDetails where id= :id")
    fun fetchMovieById(id: Int): MovieDetails

    @Query("UPDATE MovieDetails SET isFavorite = :isBookmarked where id= :id")
    fun updateFavorite(id: Int, isBookmarked: Int): Int

}