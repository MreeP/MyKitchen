package pl.aplikacjemobilne.mykitchen

import android.app.Application
import pl.aplikacjemobilne.mykitchen.data.local.database.AppDatabase
import pl.aplikacjemobilne.mykitchen.data.repository.RecipeRepository

class MyKitchenApp : Application() {
    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    val repository by lazy {
        RecipeRepository(
            recipeDao = database.recipeDao(),
            categoryDao = database.categoryDao(),
            ingredientDao = database.ingredientDao(),
            stepDao = database.stepDao(),
            favoriteDao = database.favoriteDao(),
            cookingHistoryDao = database.cookingHistoryDao(),
        )
    }
}
