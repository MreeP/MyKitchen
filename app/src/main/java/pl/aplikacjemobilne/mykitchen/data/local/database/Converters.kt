package pl.aplikacjemobilne.mykitchen.data.local.database

import androidx.room.TypeConverter
import pl.aplikacjemobilne.mykitchen.data.model.IngredientUnit

class Converters {
    @TypeConverter
    fun fromIngredientUnit(unit: IngredientUnit): String = unit.name

    @TypeConverter
    fun toIngredientUnit(value: String): IngredientUnit = IngredientUnit.valueOf(value)
}
