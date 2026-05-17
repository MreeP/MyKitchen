package pl.aplikacjemobilne.mykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeWithDetails

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes LIMIT :limit")
    fun getRecipes(limit: Int): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE categoryId = :categoryId")
    fun getByCategoryId(categoryId: Long): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getById(id: Long): Flow<RecipeEntity?>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getWithDetailsById(id: Long): Flow<RecipeWithDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<RecipeEntity>)

    @Query("DELETE FROM recipes")
    suspend fun deleteAll()
}
