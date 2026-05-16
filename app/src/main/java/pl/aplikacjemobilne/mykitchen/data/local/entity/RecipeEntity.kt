package pl.aplikacjemobilne.mykitchen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val servings: Int,
    val rating: Float,
    val authorName: String,
    val time: Int,

    val imagePath: String,
)