package pl.aplikacjemobilne.mykitchen.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.aplikacjemobilne.mykitchen.ui.screens.favorites.FavoritesScreen
import pl.aplikacjemobilne.mykitchen.ui.screens.home.HomeScreen
import pl.aplikacjemobilne.mykitchen.ui.screens.profile.ProfileScreen
import pl.aplikacjemobilne.mykitchen.ui.screens.search.SearchScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.Favorites.route) { FavoritesScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}
