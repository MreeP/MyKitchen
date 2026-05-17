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
import pl.aplikacjemobilne.mykitchen.data.local.dao.CategoryDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.CookingHistoryDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.FavoriteDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.IngredientDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.RecipeDao
import pl.aplikacjemobilne.mykitchen.data.local.dao.StepDao
import pl.aplikacjemobilne.mykitchen.data.local.entity.CategoryEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.CookingHistoryEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.FavoriteEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.IngredientEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.RecipeEntity
import pl.aplikacjemobilne.mykitchen.data.local.entity.StepEntity

@Database(
    entities = [
        CategoryEntity::class,
        RecipeEntity::class,
        IngredientEntity::class,
        StepEntity::class,
        FavoriteEntity::class,
        CookingHistoryEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun stepDao(): StepDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cookingHistoryDao(): CookingHistoryDao

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
                        )
                            .fallbackToDestructiveMigration(true)
                            .addCallback(SeedCallback())
                            .build()
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
                val db = database ?: return@launch

                db.favoriteDao().deleteAll()
                db.cookingHistoryDao().deleteAll()
                db.stepDao().deleteAll()
                db.ingredientDao().deleteAll()
                db.recipeDao().deleteAll()
                db.categoryDao().deleteAll()

                db.categoryDao().insertAll(seedCategories)
                db.recipeDao().insertAll(seedRecipes)
                db.ingredientDao().insertAll(seedIngredients)
                db.stepDao().insertAll(seedSteps)
            }
        }
    }
}

private fun resUri(resId: Int): String {
    return "android.resource://pl.aplikacjemobilne.mykitchen/$resId"
}

private val seedCategories = listOf(
    CategoryEntity(id = 1, name = "Makarony", imageUri = "🍝"),
    CategoryEntity(id = 2, name = "Sałatki", imageUri = "🥗"),
    CategoryEntity(id = 3, name = "Zupy", imageUri = "🍲"),
    CategoryEntity(id = 4, name = "Desery", imageUri = "🍰"),
    CategoryEntity(id = 5, name = "Polska", imageUri = "🇵🇱"),
)

private val seedRecipes = listOf(
    RecipeEntity(
        id = 1,
        name = "Spaghetti Carbonara",
        servings = 4,
        rating = 4.7f,
        authorName = "Anna Kowalska",
        time = 25,
        imageUri = resUri(R.drawable.img_spaghetti_carbonara),
        categoryId = 1,
    ),
    RecipeEntity(
        id = 2,
        name = "Żurek",
        servings = 6,
        rating = 4.8f,
        authorName = "Jan Nowak",
        time = 60,
        imageUri = resUri(R.drawable.img_zurek),
        categoryId = 3,
    ),
    RecipeEntity(
        id = 3,
        name = "Pierogi Ruskie",
        servings = 4,
        rating = 4.9f,
        authorName = "Maria Wiśniewska",
        time = 90,
        imageUri = resUri(R.drawable.img_pierogi_ruskie),
        categoryId = 5,
    ),
    RecipeEntity(
        id = 4,
        name = "Bigos",
        servings = 8,
        rating = 4.5f,
        authorName = "Piotr Zieliński",
        time = 120,
        imageUri = resUri(R.drawable.img_bigos),
        categoryId = 5,
    ),
    RecipeEntity(
        id = 5,
        name = "Schabowy z ziemniakami",
        servings = 2,
        rating = 4.6f,
        authorName = "Katarzyna Lewandowska",
        time = 40,
        imageUri = resUri(R.drawable.img_schabowy),
        categoryId = 5,
    ),
    RecipeEntity(
        id = 6,
        name = "Rosół",
        servings = 6,
        rating = 4.7f,
        authorName = "Tomasz Wójcik",
        time = 180,
        imageUri = resUri(R.drawable.img_rosol),
        categoryId = 3,
    ),
    RecipeEntity(
        id = 7,
        name = "Sernik",
        servings = 12,
        rating = 4.8f,
        authorName = "Agnieszka Kamińska",
        time = 75,
        imageUri = resUri(R.drawable.img_sernik),
        categoryId = 4,
    ),
    RecipeEntity(
        id = 8,
        name = "Gołąbki",
        servings = 4,
        rating = 4.4f,
        authorName = "Michał Szymański",
        time = 90,
        imageUri = resUri(R.drawable.img_golabki),
        categoryId = 5,
    ),
    RecipeEntity(
        id = 9,
        name = "Placki ziemniaczane",
        servings = 3,
        rating = 4.5f,
        authorName = "Ewa Dąbrowska",
        time = 30,
        imageUri = resUri(R.drawable.img_placki_ziemniaczane),
        categoryId = 5,
    ),
    RecipeEntity(
        id = 10,
        name = "Kotlet mielony",
        servings = 4,
        rating = 4.3f,
        authorName = "Paweł Kozłowski",
        time = 35,
        imageUri = resUri(R.drawable.img_kotlet_mielony),
        categoryId = 5,
    ),
)

private val seedIngredients = listOf(
    // Spaghetti Carbonara (1)
    IngredientEntity(recipeId = 1, name = "Spaghetti", amount = "200", unit = "g"),
    IngredientEntity(recipeId = 1, name = "Boczek pancetta", amount = "100", unit = "g"),
    IngredientEntity(recipeId = 1, name = "Żółka jaj", amount = "3", unit = "szt."),
    IngredientEntity(recipeId = 1, name = "Parmezan starty", amount = "80", unit = "g"),
    IngredientEntity(recipeId = 1, name = "Czarny pieprz", amount = "", unit = "do smaku"),

    // Żurek (2)
    IngredientEntity(recipeId = 2, name = "Zakwas żurowy", amount = "500", unit = "ml"),
    IngredientEntity(recipeId = 2, name = "Biała kiełbasa", amount = "300", unit = "g"),
    IngredientEntity(recipeId = 2, name = "Jajka", amount = "4", unit = "szt."),
    IngredientEntity(recipeId = 2, name = "Bulion", amount = "1", unit = "l"),
    IngredientEntity(recipeId = 2, name = "Majeranek", amount = "1", unit = "łyżeczka"),

    // Pierogi Ruskie (3)
    IngredientEntity(recipeId = 3, name = "Mąka pszenna", amount = "500", unit = "g"),
    IngredientEntity(recipeId = 3, name = "Ziemniaki", amount = "500", unit = "g"),
    IngredientEntity(recipeId = 3, name = "Twaróg", amount = "250", unit = "g"),
    IngredientEntity(recipeId = 3, name = "Cebula", amount = "2", unit = "szt."),
    IngredientEntity(recipeId = 3, name = "Masło", amount = "30", unit = "g"),

    // Bigos (4)
    IngredientEntity(recipeId = 4, name = "Kapusta kiszona", amount = "500", unit = "g"),
    IngredientEntity(recipeId = 4, name = "Kapusta biała", amount = "300", unit = "g"),
    IngredientEntity(recipeId = 4, name = "Kiełbasa myśliwska", amount = "200", unit = "g"),
    IngredientEntity(recipeId = 4, name = "Suszone grzyby", amount = "30", unit = "g"),
    IngredientEntity(recipeId = 4, name = "Suszone śliwki", amount = "50", unit = "g"),

    // Schabowy (5)
    IngredientEntity(recipeId = 5, name = "Schab wieprzowy", amount = "400", unit = "g"),
    IngredientEntity(recipeId = 5, name = "Jajka", amount = "2", unit = "szt."),
    IngredientEntity(recipeId = 5, name = "Bułka tarta", amount = "100", unit = "g"),
    IngredientEntity(recipeId = 5, name = "Ziemniaki", amount = "500", unit = "g"),
    IngredientEntity(recipeId = 5, name = "Olej do smażenia", amount = "", unit = "do smaku"),

    // Rosół (6)
    IngredientEntity(recipeId = 6, name = "Kurczak", amount = "1", unit = "szt."),
    IngredientEntity(recipeId = 6, name = "Marchewka", amount = "3", unit = "szt."),
    IngredientEntity(recipeId = 6, name = "Pietruszka", amount = "2", unit = "szt."),
    IngredientEntity(recipeId = 6, name = "Seler", amount = "1", unit = "szt."),
    IngredientEntity(recipeId = 6, name = "Makaron", amount = "200", unit = "g"),

    // Sernik (7)
    IngredientEntity(recipeId = 7, name = "Twaróg", amount = "1", unit = "kg"),
    IngredientEntity(recipeId = 7, name = "Cukier", amount = "200", unit = "g"),
    IngredientEntity(recipeId = 7, name = "Jajka", amount = "6", unit = "szt."),
    IngredientEntity(recipeId = 7, name = "Masło", amount = "100", unit = "g"),
    IngredientEntity(recipeId = 7, name = "Budyń waniliowy", amount = "1", unit = "opak."),

    // Gołąbki (8)
    IngredientEntity(recipeId = 8, name = "Kapusta włoska", amount = "1", unit = "szt."),
    IngredientEntity(recipeId = 8, name = "Mięso mielone", amount = "400", unit = "g"),
    IngredientEntity(recipeId = 8, name = "Ryż", amount = "150", unit = "g"),
    IngredientEntity(recipeId = 8, name = "Cebula", amount = "1", unit = "szt."),
    IngredientEntity(recipeId = 8, name = "Sos pomidorowy", amount = "500", unit = "ml"),

    // Placki ziemniaczane (9)
    IngredientEntity(recipeId = 9, name = "Ziemniaki", amount = "1", unit = "kg"),
    IngredientEntity(recipeId = 9, name = "Cebula", amount = "1", unit = "szt."),
    IngredientEntity(recipeId = 9, name = "Jajko", amount = "1", unit = "szt."),
    IngredientEntity(recipeId = 9, name = "Mąka", amount = "2", unit = "łyżki"),

    // Kotlet mielony (10)
    IngredientEntity(recipeId = 10, name = "Mięso mielone", amount = "500", unit = "g"),
    IngredientEntity(recipeId = 10, name = "Bułka", amount = "1", unit = "szt."),
    IngredientEntity(recipeId = 10, name = "Jajko", amount = "1", unit = "szt."),
    IngredientEntity(recipeId = 10, name = "Cebula", amount = "1", unit = "szt."),
    IngredientEntity(recipeId = 10, name = "Bułka tarta", amount = "50", unit = "g"),
)

private val seedSteps = listOf(
    // Spaghetti Carbonara (1)
    StepEntity(recipeId = 1, stepNumber = 1, description = "Ugotuj makaron al dente według instrukcji na opakowaniu."),
    StepEntity(recipeId = 1, stepNumber = 2, description = "Pokrój boczek w kostkę i podsmaż na suchej patelni do chrupkości."),
    StepEntity(recipeId = 1, stepNumber = 3, description = "Wymieszaj żółtka z tartym parmezanem i pieprzem."),
    StepEntity(recipeId = 1, stepNumber = 4, description = "Odcedź makaron, dodaj do boczku, zdejmij z ognia i wlej sos jajeczny mieszając."),

    // Żurek (2)
    StepEntity(recipeId = 2, stepNumber = 1, description = "Ugotuj białą kiełbasę w wodzie przez 30 minut.", timerSeconds = 1800),
    StepEntity(recipeId = 2, stepNumber = 2, description = "Ugotuj jajka na twardo (10 minut).", timerSeconds = 600),
    StepEntity(recipeId = 2, stepNumber = 3, description = "Podgrzej bulion, dodaj zakwas i majeranek."),
    StepEntity(recipeId = 2, stepNumber = 4, description = "Pokrój kiełbasę i jajka, dodaj do zupy. Podawaj gorące."),

    // Pierogi Ruskie (3)
    StepEntity(recipeId = 3, stepNumber = 1, description = "Zagnieć ciasto z mąki, wody i szczypty soli. Odstaw na 30 min.", timerSeconds = 1800),
    StepEntity(recipeId = 3, stepNumber = 2, description = "Ugotuj ziemniaki, odcedź i rozgnieć. Wymieszaj z twarogiem."),
    StepEntity(recipeId = 3, stepNumber = 3, description = "Podsmaż pokrojoną cebulę na maśle, dodaj do farszu."),
    StepEntity(recipeId = 3, stepNumber = 4, description = "Rozwałkuj ciasto, wycinaj kółka, nakładaj farsz i zlepiaj pierogi."),
    StepEntity(recipeId = 3, stepNumber = 5, description = "Gotuj pierogi w osolonej wodzie aż wypłyną na powierzchnię."),

    // Bigos (4)
    StepEntity(recipeId = 4, stepNumber = 1, description = "Namocz suszone grzyby w ciepłej wodzie na 30 minut.", timerSeconds = 1800),
    StepEntity(recipeId = 4, stepNumber = 2, description = "Pokrój kapustę białą w paseczki, podsmaż na oleju."),
    StepEntity(recipeId = 4, stepNumber = 3, description = "Połącz kapustę kiszoną z białą w dużym garnku, dodaj pokrojoną kiełbasę."),
    StepEntity(recipeId = 4, stepNumber = 4, description = "Dodaj grzyby i śliwki. Duś na małym ogniu przez 1.5 godziny.", timerSeconds = 5400),

    // Schabowy (5)
    StepEntity(recipeId = 5, stepNumber = 1, description = "Rozbij plastry schabu tłuczkiem do mięsa."),
    StepEntity(recipeId = 5, stepNumber = 2, description = "Przygotuj panierę: mąka, rozkłócone jajka, bułka tarta."),
    StepEntity(recipeId = 5, stepNumber = 3, description = "Obtocz kotlety w mące, jajku i bułce tartej."),
    StepEntity(recipeId = 5, stepNumber = 4, description = "Smaż na rozgrzanym oleju po 4 minuty z każdej strony.", timerSeconds = 480),

    // Rosół (6)
    StepEntity(recipeId = 6, stepNumber = 1, description = "Włóż kurczaka do zimnej wody, doprowadź do wrzenia, zbierz szumowiny."),
    StepEntity(recipeId = 6, stepNumber = 2, description = "Dodaj obrane warzywa: marchewkę, pietruszkę, seler."),
    StepEntity(recipeId = 6, stepNumber = 3, description = "Gotuj na małym ogniu przez 2-3 godziny.", timerSeconds = 7200),
    StepEntity(recipeId = 6, stepNumber = 4, description = "Przecedź bulion, podawaj z ugotowanym makaronem i marchewką."),

    // Sernik (7)
    StepEntity(recipeId = 7, stepNumber = 1, description = "Zmiel twaróg trzy razy przez maszynkę lub zblenduj na gładko."),
    StepEntity(recipeId = 7, stepNumber = 2, description = "Utrzyj masło z cukrem, dodawaj żółtka po jednym, mieszając."),
    StepEntity(recipeId = 7, stepNumber = 3, description = "Dodaj budyń i twaróg, na koniec delikatnie wmieszaj ubite białka."),
    StepEntity(recipeId = 7, stepNumber = 4, description = "Piecz w 170°C przez 60 minut. Nie otwieraj piekarnika!", timerSeconds = 3600),

    // Gołąbki (8)
    StepEntity(recipeId = 8, stepNumber = 1, description = "Ugotuj ryż do półmiękkości. Zblanszuj liście kapusty."),
    StepEntity(recipeId = 8, stepNumber = 2, description = "Wymieszaj mięso mielone z ryżem i posiekaną cebulą."),
    StepEntity(recipeId = 8, stepNumber = 3, description = "Nakładaj farsz na liście kapusty i zawijaj gołąbki."),
    StepEntity(recipeId = 8, stepNumber = 4, description = "Ułóż w naczyniu żaroodpornym, zalej sosem pomidorowym."),
    StepEntity(recipeId = 8, stepNumber = 5, description = "Piecz w 180°C przez 45 minut.", timerSeconds = 2700),

    // Placki ziemniaczane (9)
    StepEntity(recipeId = 9, stepNumber = 1, description = "Zetrzyj ziemniaki i cebulę na tarce o drobnych oczkach."),
    StepEntity(recipeId = 9, stepNumber = 2, description = "Odciśnij nadmiar wody, dodaj jajko i mąkę, wymieszaj."),
    StepEntity(recipeId = 9, stepNumber = 3, description = "Smaż łyżkę masy na rozgrzanym oleju z obu stron na złoty kolor."),

    // Kotlet mielony (10)
    StepEntity(recipeId = 10, stepNumber = 1, description = "Namocz bułkę w mleku, odciśnij i zmiel z mięsem i cebulą."),
    StepEntity(recipeId = 10, stepNumber = 2, description = "Dodaj jajko, sól i pieprz. Wymieszaj i uformuj kotlety."),
    StepEntity(recipeId = 10, stepNumber = 3, description = "Obtocz w bułce tartej."),
    StepEntity(recipeId = 10, stepNumber = 4, description = "Smaż na patelni po 5 minut z każdej strony.", timerSeconds = 600),
)
