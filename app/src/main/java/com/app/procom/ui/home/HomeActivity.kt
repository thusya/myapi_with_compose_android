package com.app.procom.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.app.procom.navigation.HomeDirections
import com.app.procom.navigation.NavigationManager
import com.app.procom.ui.themes.AppTheme
import com.app.procom.ui.themes.appColors
import com.app.procom.util.view.*
import org.koin.android.ext.android.inject

class HomeActivity : ComponentActivity() {

    private val navigationManager: NavigationManager by inject()
    private val viewModel: MoviesViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold()
            }
        }
    }

    @Composable
    fun Scaffold() {
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val state by navigationManager.commandFlow.collectAsState()
        val naviController = rememberNavController()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopBar() },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(onClick = {}) {
                    Text("+")
                }
            },
            bottomBar = { BottomBar() },
            content = { paddingValues ->
                AppNavigation(
                    naviController = naviController,
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel
                )
            }
        )
        if (state.navigationPath.isNotEmpty()) {
            naviController.navigate(state.navigationPath)
        }
    }

    @Composable
    fun TopBar() {
        TopAppBar(
            title = {
                Text(text = "Top App Bar")
            },
            navigationIcon = {
                IconButton(onClick = { finish() }) {
                    Icon(Icons.Filled.ArrowBack, "backIcon")
                }
            },
            backgroundColor = appColors.blue.dark,
            elevation = 10.px
        )
    }

    @Composable
    fun BottomBar() {
        val selectedIndex = remember { mutableStateOf(0) }
        BottomNavigation(elevation = 10.px) {

            BottomNavigationItem(icon = {
                Icon(imageVector = Icons.Default.Home, "")

            },
                label = { Text(text = "Movies") },
                selected = (selectedIndex.value == 0),
                onClick = { navigationManager.navigate(HomeDirections.movies) }
            )

            BottomNavigationItem(icon = {
                Icon(imageVector = Icons.Default.Favorite, "")
            },
                label = { Text(text = "Favorite") },
                selected = (selectedIndex.value == 1),
                onClick = { navigationManager.navigate(HomeDirections.favorites) })
        }
    }

    @Preview(PREVIEW_CONFIG)
    @Composable
    fun DefaultPreview() {
        AppTheme {
            Scaffold()
        }
    }
}