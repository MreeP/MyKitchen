package pl.aplikacjemobilne.mykitchen.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.aplikacjemobilne.mykitchen.data.model.IngredientUnit

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index("recipeId"),
        Index("stepNumber"),
    ],
)
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipeId: Long,
    val name: String,
    val amount: String,
    val unit: IngredientUnit,
    val stepNumber: Int? = null,
)
