package pl.aplikacjemobilne.mykitchen.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.aplikacjemobilne.mykitchen.ui.components.NamedSection
import pl.aplikacjemobilne.mykitchen.ui.components.RecipeCard

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F0EB))
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFD4540A),
                            Color(0xFFE8824A)
                        )
                    )
                )
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(240.dp)
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "🍽️",
                    fontSize = 44.sp
                )
                Text(
                    text = "DZIEŃ DOBRY, TADEUSZ",
                    color = Color(0xFFF5F0EB),
                    fontSize = 14.sp,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Co dziś ugotujemy?",
                    color = Color(0xFFF5F0EB),
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 46.sp
                )
            }
        }

        Surface(
            onClick = onNavigateToSearch,
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Color(0xFF9A9A9A),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Szukaj przepisów...",
                    color = Color(0xFF9A9A9A),
                    fontSize = 16.sp
                )
            }
        }

        NamedSection(title = "Kategorie", textModifier = Modifier.padding(start = 20.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
            ) {
                uiState.categories.forEach { category ->
                    AssistChip(
                        onClick = { onNavigateToCategory(category.name) },
                        label = {
                            Text(
                                text = category.name,
                                fontSize = 15.sp,
                            )
                        },
                        leadingIcon = {
                            Text(
                                text = category.emoji,
                                fontSize = 18.sp,
                            )
                        },
                        shape = RoundedCornerShape(percent = 50),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            labelColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        border = null,
                        elevation = AssistChipDefaults.assistChipElevation(elevation = 2.dp),
                    )
                }
            }
        }

        NamedSection(title = "Polecane dzisiaj", textModifier = Modifier.padding(start = 20.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
            ) {
                uiState.recommendedRecipes.forEach { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onNavigateToCategory(recipe.name) },
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
