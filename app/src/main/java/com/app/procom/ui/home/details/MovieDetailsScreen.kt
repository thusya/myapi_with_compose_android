package com.app.procom.ui.home.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.app.procom.ui.home.MoviesUiState
import com.app.procom.ui.home.MoviesViewModel
import com.app.procom.ui.models.MovieDetails
import com.app.procom.ui.themes.AppTheme
import com.app.procom.util.view.AppBodyText
import com.app.procom.util.view.AppTitleText
import org.koin.androidx.compose.getViewModel

@Composable
fun MovieDetailsScreen(id: Int?, viewModel: MoviesViewModel = getViewModel()) {
    AppTheme {
        val state = viewModel.listingUiState.collectAsState()
        viewModel.fetchMovieById(id ?: 0)
        DetailsScreen(state.value)
    }
}

@Composable
fun DetailsScreen(
    state: MoviesUiState = MoviesUiState.Empty
) {
    if (state is MoviesUiState.DisplayMovieDetails)
        Column(modifier = Modifier.background(Color.Gray)) {
            AppTitleText(text = state.data.date)
            AppBodyText(text = state.data.shortDescription)
            AppBodyText(text = state.data.longDescription)
        }
}

@Preview
@Composable
fun MovieDetailsScreenPreview() {
    val movieDetails = MovieDetails(
        id = 1, date = "11-12-2013",
        longDescription = " long desc", shortDescription = "shot desc"
    )
    AppTheme {
        DetailsScreen(MoviesUiState.DisplayMovieDetails(movieDetails))
    }
}