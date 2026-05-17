package pl.aplikacjemobilne.mykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.aplikacjemobilne.mykitchen.data.local.entity.CookingHistoryEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity

@Dao
interface CookingHistoryDao {
    @Query("SELECT * FROM cooking_history WHERE recipeId = :recipeId ORDER BY cookedAt DESC")
    fun getByRecipeId(recipeId: Long): Flow<List<CookingHistoryEntity>>

    @Query(
        """
        SELECT DISTINCT r.* FROM recipes r
        INNER JOIN cooking_history ch ON r.id = ch.recipeId
        ORDER BY ch.cookedAt DESC
        """
    )
    fun getCookedRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: CookingHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<CookingHistoryEntity>)

    @Query("DELETE FROM cooking_history")
    suspend fun deleteAll()
}
