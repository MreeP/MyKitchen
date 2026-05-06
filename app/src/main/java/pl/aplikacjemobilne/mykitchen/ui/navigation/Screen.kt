package pl.aplikacjemobilne.mykitchen.ui.navigation

open class Screen(val route: String) {
    data object Home: Screen("home")
    data object Search: Screen("search")
    data object Favorites: Screen("favorites")
    data object Profile: Screen("profile")
}