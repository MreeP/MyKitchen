package pl.aplikacjemobilne.mykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.aplikacjemobilne.mykitchen.data.local.entity.FavoriteEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity

@Dao
interface FavoriteDao {
    @Query(
        """
        SELECT r.* FROM recipes r
        INNER JOIN favorites f ON r.id = f.recipeId
        ORDER BY f.addedAt DESC
        """
    )
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT recipeId FROM favorites")
    fun getFavoriteRecipeIds(): Flow<List<Long>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE recipeId = :recipeId)")
    fun isFavorite(recipeId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE recipeId = :recipeId")
    suspend fun deleteByRecipeId(recipeId: Long)

    @Query("DELETE FROM favorites")
    suspend fun deleteAll()
}
