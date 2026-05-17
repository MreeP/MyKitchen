package pl.aplikacjemobilne.mykitchen.ui.screens.cooking

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import pl.aplikacjemobilne.mykitchen.data.local.entity.IngredientEntity

@Composable
fun ExpandableStepText(
    description: String,
    ingredients: List<IngredientEntity>,
    modifier: Modifier = Modifier,
    textColor: Color = Color(0xFFF5F0EB),
) {
    var expandedSet by remember { mutableStateOf(setOf<String>()) }

    val ingredientStyle = SpanStyle(
        color = Color(0xFFD4540A),
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline,
    )

    val annotated = buildAnnotatedString {
        var lastIndex = 0

        for (match in Regex("""\{([^}]+)\}""").findAll(description)) {
            append(description.substring(lastIndex, match.range.first))

            val ingredientName = match.groupValues[1]

            val ingredient = ingredients.find {
                it.name.equals(ingredientName, ignoreCase = true)
            }

            val isExpanded = ingredientName.lowercase() in expandedSet

            pushStringAnnotation(tag = "ingredient", annotation = ingredientName)

            withStyle(ingredientStyle) {
                if (isExpanded && ingredient != null) {
                    val formatted = ingredient.unit.format(ingredient.amount)

                    if (!ingredient.unit.standalone) {
                        append("$formatted $ingredientName")
                    } else {
                        append("$ingredientName ($formatted)")
                    }
                } else {
                    append(ingredientName)
                }
            }

            pop()

            lastIndex = match.range.last + 1
        }

        if (lastIndex < description.length) {
            append(description.substring(lastIndex))
        }
    }

    @Suppress("DEPRECATION")
    ClickableText(
        text = annotated,
        style = TextStyle(
            fontSize = 16.sp,
            color = textColor,
            lineHeight = 24.sp,
        ),
        modifier = modifier,
        onClick = { offset ->
            annotated
                .getStringAnnotations(tag = "ingredient", start = offset, end = offset)
                .firstOrNull()
                ?.let { annotation ->
                    val key = annotation.item.lowercase()

                    expandedSet = if (key in expandedSet) {
                        expandedSet - key
                    } else {
                        expandedSet + key
                    }
                }
        },
    )
}
