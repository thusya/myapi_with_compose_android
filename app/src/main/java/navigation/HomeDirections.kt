package navigation

import androidx.navigation.NamedNavArgument

object HomeDirections {

    val DEFAULT = object : NavCommand {
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
        override val destination: String
            get() = ""
        override var navigationPath: String
            get() = ""
            set(value) {}
    }

    val movies = object : NavCommand {
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
        override val destination: String
            get() = "movies"
        override var navigationPath: String
            get() = ""
            set(value) {}

    }

    val favorites = object : NavCommand {
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
        override val destination: String
            get() = "favorite_movies"
        override var navigationPath: String
            get() = ""
            set(value) {}
    }
}