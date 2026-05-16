package pl.aplikacjemobilne.mykitchen.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.aplikacjemobilne.mykitchen.R
import pl.aplikacjemobilne.mykitchen.data.local.dao.RecipeDao
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity

@Database(
    entities = [
        RecipeEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var database: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (this.database == null) {
                synchronized(this) {
                    if (this.database == null) {
                        this.database = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "my_kitchen_db"
                        ).addCallback(SeedCallback()).build()
                    }
                }
            }

            return this.database!!
        }
    }

    private class SeedCallback : Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)

            CoroutineScope(Dispatchers.IO).launch {
                database?.recipeDao()?.deleteAll()
                database?.recipeDao()?.insertAll(seedRecipes)
            }
        }
    }
}

private fun resUri(resId: Int): String {
    return "android.resource://pl.aplikacjemobilne.mykitchen/$resId"
}

private val seedRecipes = listOf(
    RecipeEntity(
        id = 1,
        name = "Spaghetti Carbonara",
        servings = 4,
        rating = 4.7f,
        authorName = "Anna Kowalska",
        time = 12,
        imageUri = resUri(R.drawable.img_spaghetti_carbonara)
    ),
    RecipeEntity(
        id = 2,
        name = "Żurek",
        servings = 6,
        rating = 4.8f,
        authorName = "Jan Nowak",
        time = 12,
        imageUri = resUri(R.drawable.img_zurek)
    ),
    RecipeEntity(
        id = 3,
        name = "Pierogi Ruskie",
        servings = 4,
        rating = 4.9f,
        authorName = "Maria Wiśniewska",
        time = 12,
        imageUri = resUri(R.drawable.img_pierogi_ruskie)
    ),
    RecipeEntity(
        id = 4,
        name = "Bigos",
        servings = 8,
        rating = 4.5f,
        authorName = "Piotr Zieliński",
        time = 12,
        imageUri = resUri(R.drawable.img_bigos)
    ),
    RecipeEntity(
        id = 5,
        name = "Schabowy z ziemniakami",
        servings = 2,
        rating = 4.6f,
        authorName = "Katarzyna Lewandowska",
        time = 12,
        imageUri = resUri(R.drawable.img_schabowy)
    ),
    RecipeEntity(
        id = 6,
        name = "Rosół",
        servings = 6,
        rating = 4.7f,
        authorName = "Tomasz Wójcik",
        time = 12,
        imageUri = resUri(R.drawable.img_rosol)
    ),
    RecipeEntity(
        id = 7,
        name = "Sernik",
        servings = 12,
        rating = 4.8f,
        authorName = "Agnieszka Kamińska",
        time = 12,
        imageUri = resUri(R.drawable.img_sernik)
    ),
    RecipeEntity(
        id = 8,
        name = "Gołąbki",
        servings = 4,
        rating = 4.4f,
        authorName = "Michał Szymański",
        time = 12,
        imageUri = resUri(R.drawable.img_golabki)
    ),
    RecipeEntity(
        id = 9,
        name = "Placki ziemniaczane",
        servings = 3,
        rating = 4.5f,
        authorName = "Ewa Dąbrowska",
        time = 12,
        imageUri = resUri(R.drawable.img_placki_ziemniaczane)
    ),
    RecipeEntity(
        id = 10,
        name = "Kotlet mielony",
        servings = 4,
        rating = 4.3f,
        authorName = "Paweł Kozłowski",
        time = 12,
        imageUri = resUri(R.drawable.img_kotlet_mielony)
    ),
)
