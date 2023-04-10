package com.app.procom.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import navigation.MovieDetailDirections
import navigation.NavigationManager
import com.app.procom.ui.models.MovieDetails
import com.app.procom.usecase.MoviesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val useCase: MoviesUseCase,
    private val navigationManager: NavigationManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _listingUiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
    val listingUiState = _listingUiState

    fun navigateToMovieDetails(movieDetails: MovieDetails) {
        val command = MovieDetailDirections.movieDetails
        command.navigationPath = "movie_details/${movieDetails.id}"
        navigationManager.navigate(command)
    }

    fun loadMovies() {
        viewModelScope.launch(dispatcher) {
            val movies = useCase.fetchAll()
            val state =
                if (movies.isEmpty()) MoviesUiState.Empty else MoviesUiState.DisplayMovies(data = movies)
            _listingUiState.tryEmit(state)
        }
    }

    fun loadFavoriteMovies() {
        viewModelScope.launch(dispatcher) {
            val movies = useCase.fetchFavoriteMovies()
            val state =
                if (movies.isEmpty()) MoviesUiState.Empty else MoviesUiState.DisplayMovies(data = movies)
            _listingUiState.tryEmit(state)
        }
    }

    fun fetchMovieById(id: Int) {
        viewModelScope.launch(dispatcher) {
            val movie = useCase.fetchMovieById(id)
            _listingUiState.tryEmit(MoviesUiState.DisplayMovieDetails(movie))
        }
    }

    fun bookmarkFavoriteMovie(id: Int, isBookmarked: Boolean) {
        viewModelScope.launch(dispatcher) {
            val isUpdated = useCase.updateFavoriteMovie(id, isBookmarked)
            if (isUpdated != 0) {
                loadMovies()
            } else {
                _listingUiState.tryEmit(MoviesUiState.Error("Movie Not bookmarked"))
            }
        }
    }
}