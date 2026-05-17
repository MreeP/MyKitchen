package pl.aplikacjemobilne.mykitchen.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.aplikacjemobilne.mykitchen.MyKitchenApp
import pl.aplikacjemobilne.mykitchen.data.local.entity.CategoryEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity

data class HomeUiState(
    val recommendedRecipes: List<RecipeEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MyKitchenApp).repository

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getRecommendedRecipes(5).collect { recipes ->
                _uiState.value = _uiState.value.copy(recommendedRecipes = recipes)
            }
        }

        viewModelScope.launch {
            repository.getCategories().collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories)
            }
        }
    }
}
