package pl.aplikacjemobilne.mykitchen.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import pl.aplikacjemobilne.mykitchen.MyKitchenApp
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity

enum class ProfileTab { MY_RECIPES, FAVORITES, HISTORY }

data class ProfileUiState(
    val recipes: List<RecipeEntity> = emptyList(),
    val selectedTab: ProfileTab = ProfileTab.MY_RECIPES,
)

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MyKitchenApp).repository

    private val _selectedTab = MutableStateFlow(ProfileTab.MY_RECIPES)

    val uiState: StateFlow<ProfileUiState> = _selectedTab
        .flatMapLatest { tab ->
            when (tab) {
                ProfileTab.MY_RECIPES -> repository.getRecipesByAuthor(USER_NAME)
                ProfileTab.FAVORITES -> repository.getFavoriteRecipes()
                ProfileTab.HISTORY -> repository.getCookedRecipes()
            }
        }
        .flatMapLatest { recipes ->
            MutableStateFlow(
                ProfileUiState(
                    recipes = recipes,
                    selectedTab = _selectedTab.value,
                )
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProfileUiState(),
        )

    fun selectTab(tab: ProfileTab) {
        _selectedTab.value = tab
    }

    companion object {
        const val USER_NAME = "Tadeusz Pierogowicz"
        const val USER_UNIQUE_NAME = "@tadekgotuje"
    }
}
