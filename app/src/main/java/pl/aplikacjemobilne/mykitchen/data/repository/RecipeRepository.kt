package pl.aplikacjemobilne.mykitchen.data.repository

import kotlinx.coroutines.flow.Flow
import pl.aplikacjemobilne.mykitchen.data.local.dao.RecipeDao
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity

class RecipeRepository(private val recipeDao: RecipeDao) {
    fun getRecommendedRecipes(limit: Int = 10): Flow<List<RecipeEntity>> {
        return recipeDao.getRecipes(limit)
    }
}