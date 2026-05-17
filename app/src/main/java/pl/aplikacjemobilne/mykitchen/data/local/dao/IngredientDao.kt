package pl.aplikacjemobilne.mykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.aplikacjemobilne.mykitchen.data.local.entity.IngredientEntity

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    fun getByRecipeId(recipeId: Long): Flow<List<IngredientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ingredients: List<IngredientEntity>)

    @Query("DELETE FROM ingredients")
    suspend fun deleteAll()
}
