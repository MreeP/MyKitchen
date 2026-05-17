package pl.aplikacjemobilne.mykitchen.ui.navigation

import android.net.Uri

open class Screen(val route: String) {
    data object Home : Screen(route = "home")
    data object Search : Screen(route = "search")
    data object Favorites : Screen(route = "favorites")
    data object Profile : Screen(route = "profile")

    data object Category : Screen(route = "category/{$ARG_NAME}") {
        fun routeFor(name: String): String = "category/${Uri.encode(name)}"
    }

    data object RecipeDetail : Screen(route = "recipe/{$ARG_RECIPE_ID}") {
        fun routeFor(recipeId: Long): String = "recipe/$recipeId"
    }

    data object HomeGraph : Screen(route = "home_graph")
    data object SearchGraph : Screen(route = "search_graph")
    data object FavoritesGraph : Screen(route = "favorites_graph")
    data object ProfileGraph : Screen(route = "profile_graph")

    companion object {
        const val ARG_NAME: String = "name"
        const val ARG_RECIPE_ID: String = "recipeId"
    }
}
