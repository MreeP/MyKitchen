package pl.aplikacjemobilne.mykitchen.ui.screens.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.aplikacjemobilne.mykitchen.MyKitchenApp
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity

data class FavoritesUiState(
    val recipes: List<RecipeEntity> = emptyList(),
    val favoriteIds: Set<Long> = emptySet(),
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MyKitchenApp).repository

    val uiState: StateFlow<FavoritesUiState> = combine(
        repository.getFavoriteRecipes(),
        repository.getFavoriteRecipeIds(),
    ) { recipes, ids ->
        FavoritesUiState(
            recipes = recipes,
            favoriteIds = ids.toSet(),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FavoritesUiState(),
    )

    fun toggleFavorite(recipeId: Long) {
        val isFav = uiState.value.favoriteIds.contains(recipeId)

        viewModelScope.launch {
            repository.toggleFavorite(recipeId, isFav)
        }
    }
}
