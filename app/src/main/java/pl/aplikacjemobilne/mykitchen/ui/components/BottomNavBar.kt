package pl.aplikacjemobilne.mykitchen.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import pl.aplikacjemobilne.mykitchen.ui.navigation.Screen

data class BottomNavItem(
    val label: String,
    val graphRoute: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Dom", Screen.HomeGraph.route, Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("Szukaj", Screen.SearchGraph.route, Icons.Filled.Search, Icons.Outlined.Search),
    BottomNavItem("Ulubione", Screen.FavoritesGraph.route, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    BottomNavItem("Profil", Screen.ProfileGraph.route, Icons.Filled.Person, Icons.Outlined.Person),
)

@Composable
fun BottomNavBar(
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.graphRoute } == true

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.graphRoute) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFD4540A),
                    selectedTextColor = Color(0xFFD4540A),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
