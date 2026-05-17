package pl.aplikacjemobilne.mykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.aplikacjemobilne.mykitchen.data.local.entity.CookingHistoryEntity

@Dao
interface CookingHistoryDao {
    @Query("SELECT * FROM cooking_history WHERE recipeId = :recipeId ORDER BY cookedAt DESC")
    fun getByRecipeId(recipeId: Long): Flow<List<CookingHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: CookingHistoryEntity)

    @Query("DELETE FROM cooking_history")
    suspend fun deleteAll()
}
