package pl.aplikacjemobilne.mykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.aplikacjemobilne.mykitchen.data.local.entity.StepEntity

@Dao
interface StepDao {
    @Query("SELECT * FROM steps WHERE recipeId = :recipeId ORDER BY stepNumber")
    fun getByRecipeId(recipeId: Long): Flow<List<StepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(steps: List<StepEntity>)

    @Query("DELETE FROM steps")
    suspend fun deleteAll()
}
