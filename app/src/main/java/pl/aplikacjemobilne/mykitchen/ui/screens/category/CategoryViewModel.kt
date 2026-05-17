package pl.aplikacjemobilne.mykitchen.ui.screens.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.aplikacjemobilne.mykitchen.MyKitchenApp
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity

data class CategoryUiState(
    val categoryName: String = "",
    val recipes: List<RecipeEntity> = emptyList(),
    val favoriteIds: Set<Long> = emptySet(),
)

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MyKitchenApp).repository

    private val _categoryName = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CategoryUiState> = combine(
        _categoryName
            .filterNotNull()
            .flatMapLatest { name ->
                combine(
                    repository.getCategoryByName(name),
                    MutableStateFlow(name)
                ) { category, n -> Pair(n, category) }
            }
            .flatMapLatest { (name, category) ->
                if (category != null) {
                    repository
                        .getRecipesByCategory(category.id)
                        .combine(MutableStateFlow(name)) { recipes, name -> Pair(name, recipes) }
                } else {
                    MutableStateFlow(Pair(name, emptyList<RecipeEntity>()))
                }
            },
        repository.getFavoriteRecipeIds(),
    ) { (name, recipes), favoriteIds ->
        CategoryUiState(
            categoryName = name,
            recipes = recipes,
            favoriteIds = favoriteIds.toSet(),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CategoryUiState(),
    )

    fun setCategoryName(name: String) {
        _categoryName.value = name
    }

    fun toggleFavorite(recipeId: Long) {
        val isFav = uiState.value.favoriteIds.contains(recipeId)

        viewModelScope.launch {
            repository.toggleFavorite(recipeId, isFav)
        }
    }
}
