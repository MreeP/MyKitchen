package pl.aplikacjemobilne.mykitchen.ui.screens.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.aplikacjemobilne.mykitchen.MyKitchenApp
import pl.aplikacjemobilne.mykitchen.data.local.entity.IngredientEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.StepEntity

data class RecipeDetailUiState(
    val recipe: RecipeEntity? = null,
    val ingredients: List<IngredientEntity> = emptyList(),
    val steps: List<StepEntity> = emptyList(),
    val isFavorite: Boolean = false,
)

class RecipeDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MyKitchenApp).repository

    private val _recipeId = MutableStateFlow<Long?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<RecipeDetailUiState> = _recipeId
        .filterNotNull()
        .flatMapLatest { id ->
            combine(
                repository.getRecipeWithDetails(id),
                repository.isFavorite(id),
            ) { details, isFav ->
                if (details != null) {
                    RecipeDetailUiState(
                        recipe = details.recipe,
                        ingredients = details.ingredients,
                        steps = details.steps.sortedBy { it.stepNumber },
                        isFavorite = isFav,
                    )
                } else {
                    RecipeDetailUiState()
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            RecipeDetailUiState(),
        )

    fun setRecipeId(id: Long) {
        _recipeId.value = id
    }

    fun toggleFavorite() {
        val state = uiState.value
        val recipeId = state.recipe?.id ?: return

        viewModelScope.launch {
            repository.toggleFavorite(recipeId, state.isFavorite)
        }
    }
}
