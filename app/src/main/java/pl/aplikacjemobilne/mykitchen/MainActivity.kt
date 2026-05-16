package pl.aplikacjemobilne.mykitchen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

                Scaffold(
                    contentWindowInsets = WindowInsets(0),
                    bottomBar = {
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
