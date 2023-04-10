package com.app.procom.util.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.procom.navigation.HomeDirections
import com.app.procom.navigation.MovieDetailDirections.MOVIE_ID
import com.app.procom.navigation.Screen
import com.app.procom.ui.home.MoviesViewModel
import com.app.procom.ui.home.details.MovieDetailsScreen
import com.app.procom.ui.home.listing.MovieListingScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    naviController: NavHostController = rememberNavController(),
    startDestination: String = "movies".addGraphPostfix(),
    viewModel: MoviesViewModel
) {
    NavHost(
        modifier = modifier,
        navController = naviController,
        startDestination = startDestination
    ) {
        mainGraph(viewModel)
    }
}

private fun String.addGraphPostfix(): String {
    return this.plus("_graph")
}

fun NavGraphBuilder.mainGraph(viewModel: MoviesViewModel) {
    navigation(
        startDestination = "movies",
        route = HomeDirections.movies.destination.addGraphPostfix()
    ) {
        composable("movies") {
            MovieListingScreen(
                Screen.MoviesScreen, viewModel,
                onNavigateToDetails = {
                    viewModel.navigateToMovieDetails(it)
                })
        }
        composable("favorite_movies") {
            MovieListingScreen(
                Screen.FavoritesMovieScreen, viewModel,
                onNavigateToDetails = {
                    viewModel.navigateToMovieDetails(it)
                })
        }
        composable(
            "movie_details/{movie_id}",
            arguments = listOf(navArgument(MOVIE_ID) { type = NavType.IntType })
        ) {
            MovieDetailsScreen(id = it.arguments?.getInt(MOVIE_ID), viewModel)
        }
    }
}