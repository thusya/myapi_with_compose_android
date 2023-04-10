package navigation

import navigation.HomeDirections.DEFAULT
import kotlinx.coroutines.flow.MutableStateFlow

class NavigationManager {

    private val _commandFlow = MutableStateFlow(DEFAULT)
    val commandFlow = _commandFlow

    fun navigate(directions: NavCommand) {
        _commandFlow.tryEmit(directions)
    }
}