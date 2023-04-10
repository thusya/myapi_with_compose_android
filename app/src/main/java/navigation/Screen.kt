package navigation

sealed class Screen(val route: String) {

    object MoviesScreen : Screen("movies")
    object FavoritesMovieScreen : Screen("favorite_movies")
    object MovieDetailsScreen : Screen("movie_details")
}