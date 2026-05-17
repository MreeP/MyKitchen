package pl.aplikacjemobilne.mykitchen.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "steps",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index("recipeId")
    ],
)
data class StepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipeId: Long,
    val stepNumber: Int,
    val description: String,
    val timerSeconds: Int? = null,
    val imageUri: String? = null,
)
