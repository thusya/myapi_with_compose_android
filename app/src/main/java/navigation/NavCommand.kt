package navigation

import androidx.navigation.NamedNavArgument

interface NavCommand {
    val arguments: List<NamedNavArgument>
    val destination: String
    var navigationPath: String

}