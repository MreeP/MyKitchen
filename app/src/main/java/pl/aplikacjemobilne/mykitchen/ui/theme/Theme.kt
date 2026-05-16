package pl.aplikacjemobilne.mykitchen.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MykitchenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = appColorScheme,
        typography = appTypography,
        content = content,
    )
}
