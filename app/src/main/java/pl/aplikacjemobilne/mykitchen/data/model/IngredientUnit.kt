package pl.aplikacjemobilne.mykitchen.data.model

enum class IngredientUnit(
    val displayText: String,
    val spaced: Boolean = false,
    val standalone: Boolean = false,
) {
    GRAM("g"),
    MILLILITER("ml"),
    KILOGRAM("kg"),
    LITER("l"),
    PIECE("szt.", spaced = true),
    TABLESPOON("łyżki", spaced = true),
    TEASPOON("łyżeczka", spaced = true),
    TEASPOONS("łyżeczki", spaced = true),
    PACKAGE("opak.", spaced = true),
    CLOVES("ząbki", spaced = true),
    TO_TASTE("do smaku", standalone = true),
    FOR_SPRINKLING("do posypania", standalone = true);

    fun format(amount: String): String {
        if (standalone) {
            return displayText
        }

        if (amount.isEmpty()) {
            return displayText
        }

        return amount + if (spaced) " " else "" + displayText
    }
}
