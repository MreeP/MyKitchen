package pl.aplikacjemobilne.mykitchen.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import pl.aplikacjemobilne.mykitchen.ui.screens.category.CategoryScreen
import pl.aplikacjemobilne.mykitchen.ui.screens.favorites.FavoritesScreen
import pl.aplikacjemobilne.mykitchen.ui.screens.home.HomeScreen
import pl.aplikacjemobilne.mykitchen.ui.screens.profile.ProfileScreen
import pl.aplikacjemobilne.mykitchen.ui.screens.search.SearchScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeGraph.route,
        modifier = modifier,
    ) {
        navigation(
            route = Screen.HomeGraph.route,
            startDestination = Screen.Home.route,
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToSearch = {
                        navController.navigate(Screen.SearchGraph.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToCategory = { name ->
                        navController.navigate(Screen.Category.routeFor(name))
                    },
                )
            }
            composable(
                route = Screen.Category.route,
                arguments = listOf(
                    navArgument(Screen.ARG_NAME) { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString(Screen.ARG_NAME).orEmpty()
                CategoryScreen(categoryName = name)
            }
        }
        navigation(
            route = Screen.SearchGraph.route,
            startDestination = Screen.Search.route,
        ) {
            composable(Screen.Search.route) { SearchScreen() }
        }
        navigation(
            route = Screen.FavoritesGraph.route,
            startDestination = Screen.Favorites.route,
        ) {
            composable(Screen.Favorites.route) { FavoritesScreen() }
        }
        navigation(
            route = Screen.ProfileGraph.route,
            startDestination = Screen.Profile.route,
        ) {
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}
