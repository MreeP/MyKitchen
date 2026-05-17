package pl.aplikacjemobilne.mykitchen.data.repository

import kotlinx.coroutines.flow.Flow
import pl.aplikacjemobilne.mykitchen.data.local.dao.CategoryDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.CookingHistoryDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.FavoriteDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.IngredientDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.RecipeDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.StepDao
import pl.aplikacjemobilne.mykitchen.data.local.entity.CategoryEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.CookingHistoryEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.FavoriteEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeWithDetails

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val categoryDao: CategoryDao,
    private val ingredientDao: IngredientDao,
    private val stepDao: StepDao,
    private val favoriteDao: FavoriteDao,
    private val cookingHistoryDao: CookingHistoryDao,
) {
    fun searchRecipes(query: String): Flow<List<RecipeEntity>> {
        return recipeDao.searchByName(query)
    }

    fun getRecommendedRecipes(limit: Int = 5): Flow<List<RecipeEntity>> {
        return recipeDao.getRecipes(limit)
    }

    fun getCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAll()
    }

    fun getCategoryByName(name: String): Flow<CategoryEntity?> {
        return categoryDao.getByName(name)
    }

    fun getRecipesByCategory(categoryId: Long): Flow<List<RecipeEntity>> {
        return recipeDao.getByCategoryId(categoryId)
    }

    fun getRecipeWithDetails(recipeId: Long): Flow<RecipeWithDetails?> {
        return recipeDao.getWithDetailsById(recipeId)
    }

    fun getFavoriteRecipes(): Flow<List<RecipeEntity>> {
        return favoriteDao.getFavoriteRecipes()
    }

    fun getFavoriteRecipeIds(): Flow<List<Long>> {
        return favoriteDao.getFavoriteRecipeIds()
    }

    fun isFavorite(recipeId: Long): Flow<Boolean> {
        return favoriteDao.isFavorite(recipeId)
    }

    fun getRecipesByAuthor(authorName: String): Flow<List<RecipeEntity>> {
        return recipeDao.getByAuthorName(authorName)
    }

    fun getCookedRecipes(): Flow<List<RecipeEntity>> {
        return cookingHistoryDao.getCookedRecipes()
    }

    suspend fun addCookingHistory(recipeId: Long) {
        cookingHistoryDao.insert(
            CookingHistoryEntity(
                recipeId = recipeId,
                cookedAt = System.currentTimeMillis(),
            )
        )
    }

    suspend fun toggleFavorite(recipeId: Long, currentlyFavorite: Boolean) {
        if (currentlyFavorite) {
            favoriteDao.deleteByRecipeId(recipeId)
        } else {
            favoriteDao.insert(
                FavoriteEntity(
                    recipeId = recipeId,
                    addedAt = System.currentTimeMillis(),
                )
            )
        }
    }
}
