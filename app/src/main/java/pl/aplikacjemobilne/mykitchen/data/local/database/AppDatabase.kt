package pl.aplikacjemobilne.mykitchen.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
import pl.aplikacjemobilne.mykitchen.data.model.IngredientUnit

@Database(
    entities = [
        CategoryEntity::class,
        RecipeEntity::class,
        IngredientEntity::class,
        StepEntity::class,
        FavoriteEntity::class,
        CookingHistoryEntity::class,
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
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
    // Rozkoszny.pl
    RecipeEntity(
        id = 1,
        name = "Makaron Primavera z jogurtem greckim",
        servings = 2,
        rating = 4.7f,
        authorName = "Rozkoszny.pl",
        time = 15,
        imageUri = resUri(R.drawable.img_makaron_primavera),
        categoryId = 1,
    ),
    RecipeEntity(
        id = 4,
        name = "Cytrusowe boczniaki ze szparagami i chili",
        servings = 2,
        rating = 4.7f,
        authorName = "Rozkoszny.pl",
        time = 15,
        imageUri = resUri(R.drawable.img_boczniaki_cytrusowe),
        categoryId = 2,
    ),
    RecipeEntity(
        id = 5,
        name = "Bardzo zielona zupa z cheddarowym krakersem",
        servings = 4,
        rating = 4.8f,
        authorName = "Rozkoszny.pl",
        time = 35,
        imageUri = resUri(R.drawable.img_zielona_zupa),
        categoryId = 3,
    ),
    RecipeEntity(
        id = 6,
        name = "Tom yum na skróty",
        servings = 4,
        rating = 4.6f,
        authorName = "Rozkoszny.pl",
        time = 35,
        imageUri = resUri(R.drawable.img_tom_yum),
        categoryId = 3,
    ),
    RecipeEntity(
        id = 8,
        name = "Sernik baskijski",
        servings = 12,
        rating = 4.9f,
        authorName = "Rozkoszny.pl",
        time = 60,
        imageUri = resUri(R.drawable.img_sernik_baskijski),
        categoryId = 4,
    ),
    // AniaGotuje.pl
    RecipeEntity(
        id = 2,
        name = "Makaron ze szparagami",
        servings = 4,
        rating = 4.6f,
        authorName = "AniaGotuje.pl",
        time = 40,
        imageUri = resUri(R.drawable.img_makaron_szparagi),
        categoryId = 1,
    ),
    RecipeEntity(
        id = 3,
        name = "Sałatka Gyros",
        servings = 4,
        rating = 4.5f,
        authorName = "AniaGotuje.pl",
        time = 45,
        imageUri = resUri(R.drawable.img_salatka_gyros),
        categoryId = 2,
    ),
    RecipeEntity(
        id = 7,
        name = "Barszcz ukraiński z botwinką",
        servings = 6,
        rating = 4.7f,
        authorName = "AniaGotuje.pl",
        time = 120,
        imageUri = resUri(R.drawable.img_barszcz_ukrainski),
        categoryId = 3,
    ),
    RecipeEntity(
        id = 9,
        name = "Szarlotka",
        servings = 8,
        rating = 4.8f,
        authorName = "AniaGotuje.pl",
        time = 100,
        imageUri = resUri(R.drawable.img_szarlotka),
        categoryId = 4,
    ),
    RecipeEntity(
        id = 10,
        name = "Racuchy z jabłkami",
        servings = 4,
        rating = 4.5f,
        authorName = "AniaGotuje.pl",
        time = 30,
        imageUri = resUri(R.drawable.img_racuchy_jablka),
        categoryId = 4,
    ),
    RecipeEntity(
        id = 11,
        name = "Fasolka po bretońsku",
        servings = 10,
        rating = 4.6f,
        authorName = "AniaGotuje.pl",
        time = 150,
        imageUri = resUri(R.drawable.img_fasolka_bretonska),
        categoryId = 5,
    ),
)

private val seedIngredients = listOf(
    // Makaron Primavera z jogurtem greckim (1) – Rozkoszny.pl
    IngredientEntity(recipeId = 1, name = "Spaghetti", amount = "200", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 1, name = "Szparagi zielone", amount = "250", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 1, name = "Masło", amount = "2", unit = IngredientUnit.TABLESPOON, stepNumber = 3),
    IngredientEntity(recipeId = 1, name = "Dymka", amount = "2", unit = IngredientUnit.PIECE, stepNumber = 3),
    IngredientEntity(recipeId = 1, name = "Groszek cukrowy", amount = "125", unit = IngredientUnit.GRAM, stepNumber = 3),
    IngredientEntity(recipeId = 1, name = "Czosnek", amount = "4", unit = IngredientUnit.CLOVES, stepNumber = 4),
    IngredientEntity(recipeId = 1, name = "Jogurt grecki", amount = "250", unit = IngredientUnit.GRAM, stepNumber = 4),
    IngredientEntity(recipeId = 1, name = "Parmezan", amount = "60", unit = IngredientUnit.GRAM, stepNumber = 4),
    IngredientEntity(recipeId = 1, name = "Skórka z cytryny", amount = "1", unit = IngredientUnit.TABLESPOON, stepNumber = 4),

    // Makaron ze szparagami (2) – AniaGotuje.pl
    IngredientEntity(recipeId = 2, name = "Makaron pappardelle", amount = "250", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 2, name = "Szparagi zielone", amount = "350", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 2, name = "Boczek wędzony", amount = "150", unit = IngredientUnit.GRAM, stepNumber = 3),
    IngredientEntity(recipeId = 2, name = "Czosnek", amount = "1", unit = IngredientUnit.CLOVES, stepNumber = 4),
    IngredientEntity(recipeId = 2, name = "Śmietanka 30%", amount = "5", unit = IngredientUnit.TABLESPOON, stepNumber = 4),
    IngredientEntity(recipeId = 2, name = "Parmezan", amount = "20", unit = IngredientUnit.GRAM, stepNumber = 4),

    // Sałatka Gyros (3) – AniaGotuje.pl
    IngredientEntity(recipeId = 3, name = "Pierś z kurczaka", amount = "800", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 3, name = "Przyprawa gyros", amount = "30", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 3, name = "Jogurt naturalny", amount = "4", unit = IngredientUnit.TABLESPOON, stepNumber = 2),
    IngredientEntity(recipeId = 3, name = "Majonez", amount = "2", unit = IngredientUnit.TABLESPOON, stepNumber = 2),
    IngredientEntity(recipeId = 3, name = "Czosnek", amount = "2", unit = IngredientUnit.CLOVES, stepNumber = 2),
    IngredientEntity(recipeId = 3, name = "Kukurydza konserwowa", amount = "400", unit = IngredientUnit.GRAM, stepNumber = 3),
    IngredientEntity(recipeId = 3, name = "Ogórek kiszony", amount = "150", unit = IngredientUnit.GRAM, stepNumber = 3),
    IngredientEntity(recipeId = 3, name = "Pomidorki koktajlowe", amount = "150", unit = IngredientUnit.GRAM, stepNumber = 3),
    IngredientEntity(recipeId = 3, name = "Papryka", amount = "1", unit = IngredientUnit.PIECE, stepNumber = 3),
    IngredientEntity(recipeId = 3, name = "Sałata lodowa", amount = "150", unit = IngredientUnit.GRAM, stepNumber = 4),

    // Cytrusowe boczniaki ze szparagami i chili (4) – Rozkoszny.pl
    IngredientEntity(recipeId = 4, name = "Szparagi", amount = "500", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 4, name = "Boczniaki", amount = "250", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 4, name = "Czosnek", amount = "3", unit = IngredientUnit.CLOVES, stepNumber = 2),
    IngredientEntity(recipeId = 4, name = "Chili czerwone", amount = "1", unit = IngredientUnit.PIECE, stepNumber = 2),
    IngredientEntity(recipeId = 4, name = "Sos sojowy", amount = "2", unit = IngredientUnit.TABLESPOON, stepNumber = 3),
    IngredientEntity(recipeId = 4, name = "Masło", amount = "2", unit = IngredientUnit.TABLESPOON, stepNumber = 3),
    IngredientEntity(recipeId = 4, name = "Pomarańcza", amount = "1", unit = IngredientUnit.PIECE, stepNumber = 3),
    IngredientEntity(recipeId = 4, name = "Sezam", amount = "", unit = IngredientUnit.FOR_SPRINKLING, stepNumber = 4),

    // Bardzo zielona zupa z cheddarowym krakersem (5) – Rozkoszny.pl
    IngredientEntity(recipeId = 5, name = "Por", amount = "2", unit = IngredientUnit.PIECE, stepNumber = 1),
    IngredientEntity(recipeId = 5, name = "Masło", amount = "2", unit = IngredientUnit.TABLESPOON, stepNumber = 1),
    IngredientEntity(recipeId = 5, name = "Czosnek", amount = "4", unit = IngredientUnit.CLOVES, stepNumber = 2),
    IngredientEntity(recipeId = 5, name = "Ziemniak", amount = "1", unit = IngredientUnit.PIECE, stepNumber = 2),
    IngredientEntity(recipeId = 5, name = "Bulion warzywny", amount = "1", unit = IngredientUnit.LITER, stepNumber = 2),
    IngredientEntity(recipeId = 5, name = "Groszek mrożony", amount = "400", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 5, name = "Śmietana 30%", amount = "60", unit = IngredientUnit.MILLILITER, stepNumber = 3),
    IngredientEntity(recipeId = 5, name = "Cheddar dojrzały", amount = "200", unit = IngredientUnit.GRAM, stepNumber = 4),

    // Tom yum na skróty (6) – Rozkoszny.pl
    IngredientEntity(recipeId = 6, name = "Trawa cytrynowa", amount = "4", unit = IngredientUnit.PIECE, stepNumber = 1),
    IngredientEntity(recipeId = 6, name = "Cebula czerwona", amount = "1", unit = IngredientUnit.PIECE, stepNumber = 2),
    IngredientEntity(recipeId = 6, name = "Imbir", amount = "1", unit = IngredientUnit.PIECE, stepNumber = 2),
    IngredientEntity(recipeId = 6, name = "Pasta tom kha", amount = "50", unit = IngredientUnit.GRAM, stepNumber = 3),
    IngredientEntity(recipeId = 6, name = "Mleko kokosowe", amount = "400", unit = IngredientUnit.MILLILITER, stepNumber = 3),
    IngredientEntity(recipeId = 6, name = "Sos rybny", amount = "1", unit = IngredientUnit.TABLESPOON, stepNumber = 3),
    IngredientEntity(recipeId = 6, name = "Pieczarki", amount = "150", unit = IngredientUnit.GRAM, stepNumber = 3),
    IngredientEntity(recipeId = 6, name = "Krewetki", amount = "220", unit = IngredientUnit.GRAM, stepNumber = 4),
    IngredientEntity(recipeId = 6, name = "Pomidorki koktajlowe", amount = "150", unit = IngredientUnit.GRAM, stepNumber = 4),
    IngredientEntity(recipeId = 6, name = "Pak choy", amount = "1", unit = IngredientUnit.PIECE, stepNumber = 4),

    // Barszcz ukraiński z botwinką (7) – AniaGotuje.pl
    IngredientEntity(recipeId = 7, name = "Ćwiartka z kurczaka", amount = "450", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 7, name = "Włoszczyzna", amount = "1", unit = IngredientUnit.PACKAGE, stepNumber = 1),
    IngredientEntity(recipeId = 7, name = "Botwinka z burakami", amount = "500", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 7, name = "Ziemniaki", amount = "300", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 7, name = "Fasolka szparagowa", amount = "250", unit = IngredientUnit.GRAM, stepNumber = 3),
    IngredientEntity(recipeId = 7, name = "Czosnek", amount = "2", unit = IngredientUnit.CLOVES, stepNumber = 3),
    IngredientEntity(recipeId = 7, name = "Młoda kapusta", amount = "300", unit = IngredientUnit.GRAM, stepNumber = 4),
    IngredientEntity(recipeId = 7, name = "Masło", amount = "20", unit = IngredientUnit.GRAM, stepNumber = 4),
    IngredientEntity(recipeId = 7, name = "Sok z cytryny", amount = "3", unit = IngredientUnit.TABLESPOON, stepNumber = 5),

    // Sernik baskijski (8) – Rozkoszny.pl
    IngredientEntity(recipeId = 8, name = "Herbatniki", amount = "180", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 8, name = "Masło", amount = "3", unit = IngredientUnit.TABLESPOON, stepNumber = 1),
    IngredientEntity(recipeId = 8, name = "Serek kremowy", amount = "750", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 8, name = "Jogurt grecki", amount = "250", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 8, name = "Cukier", amount = "250", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 8, name = "Jajka", amount = "6", unit = IngredientUnit.PIECE, stepNumber = 2),
    IngredientEntity(recipeId = 8, name = "Śmietanka 36%", amount = "400", unit = IngredientUnit.MILLILITER, stepNumber = 3),
    IngredientEntity(recipeId = 8, name = "Ekstrakt waniliowy", amount = "1", unit = IngredientUnit.TEASPOON, stepNumber = 3),

    // Szarlotka (9) – AniaGotuje.pl
    IngredientEntity(recipeId = 9, name = "Mąka pszenna", amount = "320", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 9, name = "Masło", amount = "200", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 9, name = "Jajko", amount = "1", unit = IngredientUnit.PIECE, stepNumber = 1),
    IngredientEntity(recipeId = 9, name = "Cukier", amount = "80", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 9, name = "Proszek do pieczenia", amount = "2", unit = IngredientUnit.TEASPOONS, stepNumber = 1),
    IngredientEntity(recipeId = 9, name = "Jabłka", amount = "1.5", unit = IngredientUnit.KILOGRAM, stepNumber = 2),
    IngredientEntity(recipeId = 9, name = "Cynamon", amount = "1", unit = IngredientUnit.TEASPOON, stepNumber = 2),

    // Racuchy z jabłkami (10) – AniaGotuje.pl
    IngredientEntity(recipeId = 10, name = "Jajka", amount = "2", unit = IngredientUnit.PIECE, stepNumber = 1),
    IngredientEntity(recipeId = 10, name = "Mleko", amount = "250", unit = IngredientUnit.MILLILITER, stepNumber = 1),
    IngredientEntity(recipeId = 10, name = "Mąka pszenna", amount = "250", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 10, name = "Proszek do pieczenia", amount = "½", unit = IngredientUnit.TEASPOONS, stepNumber = 2),
    IngredientEntity(recipeId = 10, name = "Jabłka", amount = "2", unit = IngredientUnit.PIECE, stepNumber = 3),
    IngredientEntity(recipeId = 10, name = "Olej do smażenia", amount = "4", unit = IngredientUnit.TABLESPOON, stepNumber = 4),
    IngredientEntity(recipeId = 10, name = "Cukier puder", amount = "", unit = IngredientUnit.FOR_SPRINKLING, stepNumber = 5),

    // Fasolka po bretońsku (11) – AniaGotuje.pl
    IngredientEntity(recipeId = 11, name = "Fasola Piękny Jaś", amount = "500", unit = IngredientUnit.GRAM, stepNumber = 1),
    IngredientEntity(recipeId = 11, name = "Boczek wędzony", amount = "300", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 11, name = "Kiełbasa", amount = "400", unit = IngredientUnit.GRAM, stepNumber = 2),
    IngredientEntity(recipeId = 11, name = "Cebula", amount = "1", unit = IngredientUnit.PIECE, stepNumber = 3),
    IngredientEntity(recipeId = 11, name = "Czosnek", amount = "5", unit = IngredientUnit.CLOVES, stepNumber = 3),
    IngredientEntity(recipeId = 11, name = "Pomidory", amount = "1500", unit = IngredientUnit.GRAM, stepNumber = 4),
    IngredientEntity(recipeId = 11, name = "Koncentrat pomidorowy", amount = "40", unit = IngredientUnit.GRAM, stepNumber = 4),
    IngredientEntity(recipeId = 11, name = "Majeranek", amount = "1", unit = IngredientUnit.TABLESPOON, stepNumber = 4),
)

private val seedSteps = listOf(
    // Makaron Primavera z jogurtem greckim (1) – Rozkoszny.pl
    StepEntity(recipeId = 1, stepNumber = 1, description = "Ugotuj {spaghetti} al dente w osolonej wodzie. Zachowaj ½ szklanki wody z gotowania."),
    StepEntity(recipeId = 1, stepNumber = 2, description = "Odłam zdrewniałe końce {szparagi zielone} i pokrój na kawałki po 2 cm."),
    StepEntity(recipeId = 1, stepNumber = 3, description = "Rozgrzej {masło} na patelni. Podsmaż {dymka} z {groszek cukrowy} i szparagami przez 2-3 minuty."),
    StepEntity(recipeId = 1, stepNumber = 4, description = "Dodaj wyciśnięty {czosnek}, zdejmij z ognia. Wmieszaj makaron, {jogurt grecki}, {parmezan} i {skórka z cytryny}. Dolewaj wodę z gotowania do uzyskania kremowej konsystencji."),

    // Makaron ze szparagami (2) – AniaGotuje.pl
    StepEntity(recipeId = 2, stepNumber = 1, description = "Ugotuj {makaron pappardelle} al dente w osolonej wodzie. Zachowaj pół szklanki wody z gotowania."),
    StepEntity(recipeId = 2, stepNumber = 2, description = "Opłucz {szparagi zielone}, odłam zdrewniałe końce i pokrój na kawałki."),
    StepEntity(recipeId = 2, stepNumber = 3, description = "Pokrój {boczek wędzony} w kostkę i smaż na suchej patelni aż puści tłuszcz. Dodaj szparagi i smaż razem 15 minut.", timerSeconds = 900),
    StepEntity(recipeId = 2, stepNumber = 4, description = "Dodaj wyciśnięty {czosnek}, sól, pieprz i gałkę muszkatołową. Wlej {śmietanka 30%} i wodę z gotowania, dodaj {parmezan}. Wymieszaj z makaronem."),

    // Sałatka Gyros (3) – AniaGotuje.pl
    StepEntity(recipeId = 3, stepNumber = 1, description = "Pokrój {pierś z kurczaka} w kawałki, obtocz w {przyprawa gyros}. Smaż na patelni przez 10 minut. Ostudź.", timerSeconds = 600),
    StepEntity(recipeId = 3, stepNumber = 2, description = "Przygotuj sos: wymieszaj {jogurt naturalny}, {majonez}, wyciśnięty {czosnek} i sok z cytryny."),
    StepEntity(recipeId = 3, stepNumber = 3, description = "Na dno naczynia ułóż kurczaka. Nakładaj warstwy: {ogórek kiszony}, {kukurydza konserwowa}, {pomidorki koktajlowe} i pokrojoną {papryka}."),
    StepEntity(recipeId = 3, stepNumber = 4, description = "Polej sosem czosnkowym i przykryj posiekaną {sałata lodowa}."),

    // Cytrusowe boczniaki ze szparagami i chili (4) – Rozkoszny.pl
    StepEntity(recipeId = 4, stepNumber = 1, description = "Odłam zdrewniałe końce {szparagi} i pokrój na kawałki po 2 cm."),
    StepEntity(recipeId = 4, stepNumber = 2, description = "Rozgrzej olej na dużym ogniu. Smaż {boczniaki} przez 4 minuty. Dodaj {czosnek} i {chili czerwone}, potem szparagi.", timerSeconds = 240),
    StepEntity(recipeId = 4, stepNumber = 3, description = "Wlej {sos sojowy} z odrobiną wody i {masło}. Mieszaj aż masło się wchłonie. Skrop sokiem z {pomarańcza}."),
    StepEntity(recipeId = 4, stepNumber = 4, description = "Podawaj posypane {sezam} i kolendrą. Pasuje do ryżu jaśminowego."),

    // Bardzo zielona zupa z cheddarowym krakersem (5) – Rozkoszny.pl
    StepEntity(recipeId = 5, stepNumber = 1, description = "Rozpuść {masło} w garnku. Podsmaż pokrojony {por} na małym ogniu przez 8-10 minut.", timerSeconds = 600),
    StepEntity(recipeId = 5, stepNumber = 2, description = "Dodaj {czosnek}, smaż minutę. Dodaj pokrojony {ziemniak} i {bulion warzywny}. Gotuj 15 minut, dodaj {groszek mrożony} na 2-3 minuty.", timerSeconds = 900),
    StepEntity(recipeId = 5, stepNumber = 3, description = "Zblenduj na gładko, dodaj {śmietana 30%} i sok z cytryny. Dopraw solą i pieprzem."),
    StepEntity(recipeId = 5, stepNumber = 4, description = "Posyp starty {cheddar dojrzały} na suchej patelni, dodaj sezam i chili. Smaż aż brzegi się zrumienią. Podawaj krakersy na zupie."),

    // Tom yum na skróty (6) – Rozkoszny.pl
    StepEntity(recipeId = 6, stepNumber = 1, description = "Rozgnieć {trawa cytrynowa} wałkiem i pokrój na kawałki po 3 cm."),
    StepEntity(recipeId = 6, stepNumber = 2, description = "Podsmaż pokrojoną {cebula czerwona} z trawą cytrynową i plastrami {imbir} przez 6 minut.", timerSeconds = 360),
    StepEntity(recipeId = 6, stepNumber = 3, description = "Dodaj {pasta tom kha} i płatki chili. Wlej {mleko kokosowe} z wodą, dodaj {sos rybny} i {pieczarki}. Gotuj 10 minut.", timerSeconds = 600),
    StepEntity(recipeId = 6, stepNumber = 4, description = "Dodaj {krewetki}, {pomidorki koktajlowe} i {pak choy}. Gotuj 7 minut. Podawaj z makaronem ryżowym i kolendrą.", timerSeconds = 420),

    // Barszcz ukraiński z botwinką (7) – AniaGotuje.pl
    StepEntity(recipeId = 7, stepNumber = 1, description = "Włóż {ćwiartka z kurczaka} z {włoszczyzna}, liśćmi laurowymi i zielem angielskim do 1.5l zimnej wody. Gotuj godzinę na małym ogniu.", timerSeconds = 3600),
    StepEntity(recipeId = 7, stepNumber = 2, description = "Przecedź bulion. Dodaj starte {botwinka z burakami} i pokrojone {ziemniaki}. Gotuj 5-8 minut.", timerSeconds = 480),
    StepEntity(recipeId = 7, stepNumber = 3, description = "Dodaj wyciśnięty {czosnek} i pokrojoną {fasolka szparagowa}. Gotuj 5-8 minut.", timerSeconds = 480),
    StepEntity(recipeId = 7, stepNumber = 4, description = "Dodaj poszatkowaną {młoda kapusta} i posiekaną nać botwinki z {masło}. Gotuj 5-8 minut.", timerSeconds = 480),
    StepEntity(recipeId = 7, stepNumber = 5, description = "Dopraw {sok z cytryny}, solą, pieprzem i koperkiem. Podawaj ze śmietaną i świeżym chlebem."),

    // Sernik baskijski (8) – Rozkoszny.pl
    StepEntity(recipeId = 8, stepNumber = 1, description = "Zmiel {herbatniki} na proszek, wymieszaj z roztopionym {masło}. Wyłóż na dno tortownicy i piecz 6 minut w 200°C.", timerSeconds = 360),
    StepEntity(recipeId = 8, stepNumber = 2, description = "Wymieszaj {serek kremowy} z {jogurt grecki} i {cukier} na gładką masę. Dodawaj {jajka} po jednym, mieszając."),
    StepEntity(recipeId = 8, stepNumber = 3, description = "Wlej {śmietanka 36%}, sól i {ekstrakt waniliowy}. Wymieszaj i wlej na schłodzoną bazę."),
    StepEntity(recipeId = 8, stepNumber = 4, description = "Piecz w 240°C przez 30 minut aż wierzch skarmelizuje, a środek drży. Chłódź w lodówce minimum 8 godzin.", timerSeconds = 1800),

    // Szarlotka (9) – AniaGotuje.pl
    StepEntity(recipeId = 9, stepNumber = 1, description = "Wymieszaj {mąka pszenna}, {jajko}, {cukier}, pokrojone {masło} i {proszek do pieczenia}. Zagnieć ciasto i schłódź w lodówce godzinę.", timerSeconds = 3600),
    StepEntity(recipeId = 9, stepNumber = 2, description = "Obierz {jabłka}, zetrzyj na tarce o grubych oczkach i odciśnij sok. Dodaj {cynamon}."),
    StepEntity(recipeId = 9, stepNumber = 3, description = "Podziel ciasto na pół. Pierwszą połowę wyłóż na dno formy wyłożonej papierem."),
    StepEntity(recipeId = 9, stepNumber = 4, description = "Rozłóż jabłka na cieście. Drugą połowę ciasta zetrzyj na tarce na wierzch."),
    StepEntity(recipeId = 9, stepNumber = 5, description = "Piecz w 180°C przez 70 minut aż się zarumieni. Posyp cukrem pudrem.", timerSeconds = 4200),

    // Racuchy z jabłkami (10) – AniaGotuje.pl
    StepEntity(recipeId = 10, stepNumber = 1, description = "Rozbij {jajka} do miski, dodaj ciepłe {mleko} i wymieszaj trzepaczką."),
    StepEntity(recipeId = 10, stepNumber = 2, description = "Dodaj {mąka pszenna} przesianą z {proszek do pieczenia}. Wymieszaj na gładkie ciasto."),
    StepEntity(recipeId = 10, stepNumber = 3, description = "Obierz {jabłka} i zetrzyj na tarce o grubych oczkach. Wmieszaj do ciasta."),
    StepEntity(recipeId = 10, stepNumber = 4, description = "Smaż porcje ciasta na rozgrzanym {olej do smażenia} po 1.5-2 minuty z każdej strony na złoty kolor.", timerSeconds = 240),
    StepEntity(recipeId = 10, stepNumber = 5, description = "Gotowe racuchy posyp {cukier puder}. Podawaj ciepłe."),

    // Fasolka po bretońsku (11) – AniaGotuje.pl
    StepEntity(recipeId = 11, stepNumber = 1, description = "Namocz {fasola Piękny Jaś} na noc w zimnej wodzie. Opłucz, gotuj z liśćmi laurowymi w 1l wody ok. 90 minut aż zmiękną.", timerSeconds = 5400),
    StepEntity(recipeId = 11, stepNumber = 2, description = "Pokrój {boczek wędzony} w kostkę i smaż na suchej patelni aż puści tłuszcz. Dodaj pokrojoną {kiełbasa}."),
    StepEntity(recipeId = 11, stepNumber = 3, description = "Dodaj posiekaną {cebula} i pokrojony {czosnek}. Smaż razem 10 minut.", timerSeconds = 600),
    StepEntity(recipeId = 11, stepNumber = 4, description = "Dodaj pokrojone {pomidory} z {koncentrat pomidorowy}, {majeranek}, paprykę wędzoną i pieprz. Przełóż do garnka z fasolą i gotuj 15 minut.", timerSeconds = 900),
)
