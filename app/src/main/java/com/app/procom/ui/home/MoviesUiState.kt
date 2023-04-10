package com.app.procom.ui.home

import com.app.procom.ui.models.MovieDetails

sealed interface MoviesUiState {
    object Empty: MoviesUiState
    object Loading : MoviesUiState
    class DisplayMovies(val data: List<MovieDetails>) : MoviesUiState
    class DisplayFavoriteMovies(val data: List<MovieDetails>) : MoviesUiState
    class DisplayMovieDetails(val data: MovieDetails) : MoviesUiState
    class Error(val message: String) : MoviesUiState
}