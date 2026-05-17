package pl.aplikacjemobilne.mykitchen.ui.screens.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.aplikacjemobilne.mykitchen.MyKitchenApp
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity

data class SearchUiState(
    val query: String = "",
    val recipes: List<RecipeEntity> = emptyList(),
    val favoriteIds: Set<Long> = emptySet(),
)

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MyKitchenApp).repository

    private val _query = MutableStateFlow("")

    private val recipes = _query.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.getRecommendedRecipes(5)
        } else {
            repository.searchRecipes(query.trim())
        }
    }

    val uiState: StateFlow<SearchUiState> = combine(
        _query,
        recipes,
        repository.getFavoriteRecipeIds(),
    ) { query, recipes, favoriteIds ->
        SearchUiState(
            query = query,
            recipes = recipes,
            favoriteIds = favoriteIds.toSet(),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SearchUiState(),
    )

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun toggleFavorite(recipeId: Long) {
        val isFav = uiState.value.favoriteIds.contains(recipeId)
        viewModelScope.launch {
            repository.toggleFavorite(recipeId, isFav)
        }
    }
}
