package pl.aplikacjemobilne.mykitchen

import android.app.Application
import pl.aplikacjemobilne.mykitchen.data.local.database.AppDatabase
import pl.aplikacjemobilne.mykitchen.data.repository.RecipeRepository
import kotlin.getValue


class MyKitchenApp : Application() {
    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    val repository by lazy {
        RecipeRepository(database.recipeDao())
    }
}