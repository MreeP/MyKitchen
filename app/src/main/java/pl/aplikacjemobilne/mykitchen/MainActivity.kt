package pl.aplikacjemobilne.mykitchen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pl.aplikacjemobilne.mykitchen.ui.components.BottomNavBar
import pl.aplikacjemobilne.mykitchen.ui.navigation.NavGraph
import pl.aplikacjemobilne.mykitchen.ui.theme.MykitchenTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MykitchenTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                DisposableEffect(navController) {
                    val listener = NavController.OnDestinationChangedListener { _, dest, _ ->
                        Log.d("Nav", "DEST_CHANGED → ${dest.route}")
                    }

                    navController.addOnDestinationChangedListener(listener)

                    onDispose {
                        navController.removeOnDestinationChangedListener(listener)
                    }
                }

                val isCookingScreen = currentDestination?.route == "cooking/{recipeId}"
                val activity = LocalContext.current as ComponentActivity

                LaunchedEffect(isCookingScreen) {
                    if (isCookingScreen) {
                        activity.enableEdgeToEdge(
                            navigationBarStyle = SystemBarStyle.dark(
                                android.graphics.Color.TRANSPARENT
                            )
                        )
                    } else {
                        activity.enableEdgeToEdge()
                    }
                }

                Scaffold(
                    contentWindowInsets = WindowInsets(0),
                    containerColor = if (isCookingScreen) {
                        Color(0xFF1A1200)
                    } else {
                        Color.Unspecified
                    },
                    bottomBar = {
                        if (isCookingScreen) {
                            return@Scaffold
                        }

                        BottomNavBar(
                            currentDestination = currentDestination,
                            onNavigate = { graphRoute ->
                                val alreadyInGraph = currentDestination?.hierarchy?.any {
                                    it.route == graphRoute
                                }

                                if (alreadyInGraph == true) {
                                    navController.navigate(graphRoute) {
                                        popUpTo(graphRoute) {
                                            inclusive = true
                                        }

                                        launchSingleTop = true
                                    }
                                } else {
                                    navController.navigate(graphRoute) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }

                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
