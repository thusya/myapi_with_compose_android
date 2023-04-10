package com.app.procom.ui.home.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import navigation.Screen
import com.app.procom.ui.home.MoviesUiState
import com.app.procom.ui.home.MoviesViewModel
import com.app.procom.ui.models.MovieDetails
import com.app.procom.ui.themes.AppTheme
import com.app.procom.util.view.*
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.koin.androidx.compose.getViewModel


@Composable
fun MovieListingScreen(
    movieScreenType: Screen = Screen.MoviesScreen,
    viewModel: MoviesViewModel =  getViewModel(),
    onNavigateToDetails: (MovieDetails) -> Unit = {}
) {
    if (movieScreenType == Screen.MoviesScreen) viewModel.loadMovies() else viewModel.loadFavoriteMovies()
    AppTheme {
        val state = viewModel.listingUiState.collectAsState()
        val onFavoriteClick: (MovieDetails) -> Unit =
            { movie -> viewModel.bookmarkFavoriteMovie(movie.id, !movie.isBookmarked) }
        MovieListingContent(state.value, onNavigateToDetails , onFavoriteClick)
    }
}

@Composable
fun MovieListingContent(
    state: MoviesUiState = MoviesUiState.Empty,
    onNavigateToDetails: (MovieDetails) -> Unit = {},
    onFavoriteClick: (MovieDetails) -> Unit = {}
) {
    when (state) {
        MoviesUiState.Loading -> {
            AppCircularProgressBar()
        }
        is MoviesUiState.DisplayMovies -> {
            DisplayMoviesList(state.data, onNavigateToDetails, onFavoriteClick)
        }
        is MoviesUiState.DisplayFavoriteMovies -> {
            DisplayMoviesList(
                state.data,
                onNavigateToDetails,
                onFavoriteClick,
                showBookmark = false
            )
        }
        MoviesUiState.Empty -> {
            AppBodyText("There are no Movies")
        }
        is MoviesUiState.Error -> {
            // show toast here with error
        }
        else -> {

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DisplayMoviesList(
    movies: List<MovieDetails>,
    onNavigateToDetails: (MovieDetails) -> Unit,
    onFavoriteClick: (MovieDetails) -> Unit,
    showBookmark: Boolean = true
) {
    AppLazyColumn(
        items = movies,
        onItemClick = onNavigateToDetails,
        modifier = Modifier.background(Color.White),
        key = { id }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                modifier = Modifier.size(100.px),
                model = it.images,
                contentDescription = null
            )
            Column(modifier = Modifier.weight(1f)) {
                AppTitleText(text = it.shortDescription)
                AppBodyText(text = it.longDescription, maxLines = 2)
            }
            if (showBookmark) {
                val icon =
                    if (it.isBookmarked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                Icon(imageVector = icon, "", modifier = Modifier.clickable { onFavoriteClick(it) })
            }
        }
    }
}

@Preview
@Composable
fun ListingScreenPreview() {
    val list = mutableListOf<MovieDetails>()
    list += MovieDetails(id = 1, isBookmarked = true)
    list += MovieDetails(id = 2)
    list += MovieDetails(id = 3, isBookmarked = true)
    list += MovieDetails(id = 4)
    list += MovieDetails(id = 5, isBookmarked = true)
    AppTheme {
        MovieListingContent(state = MoviesUiState.DisplayMovies(list))
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteMoviesScreenPreview() {
    val list = mutableListOf<MovieDetails>()
    list += MovieDetails(id = 1)
    list += MovieDetails(id = 2)
    list += MovieDetails(id = 3)
    list += MovieDetails(id = 4)
    list += MovieDetails(id = 5)
    AppTheme {
        MovieListingContent(state = MoviesUiState.DisplayFavoriteMovies(list))
    }
}
