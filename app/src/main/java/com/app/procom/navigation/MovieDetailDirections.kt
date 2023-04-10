package com.app.procom.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object MovieDetailDirections {

    const val MOVIE_ID = "movie_id"

    val movieDetails = object : NavCommand {
        override val arguments: List<NamedNavArgument>
            get() = listOf(navArgument(MOVIE_ID) { type = NavType.IntType })
        override val destination: String
            get() = "movie_details/{$MOVIE_ID}"
        override var navigationPath = ""
    }
}