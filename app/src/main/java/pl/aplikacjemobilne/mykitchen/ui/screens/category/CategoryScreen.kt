package pl.aplikacjemobilne.mykitchen.ui.screens.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.aplikacjemobilne.mykitchen.ui.components.RecipeListCard

@Composable
fun CategoryScreen(
    categoryName: String,
    onNavigateBack: () -> Unit,
    onNavigateToRecipe: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(categoryName) {
        viewModel.setCategoryName(categoryName)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F0EB))
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Wróć",
                    tint = Color(0xFF1A1A1A),
                )
            }
            Text(
                text = uiState.categoryName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF1A1A1A),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 48.dp),
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        ) {
            items(uiState.recipes, key = { it.id }) { recipe ->
                RecipeListCard(
                    recipe = recipe,
                    isFavorite = uiState.favoriteIds.contains(recipe.id),
                    onClick = { onNavigateToRecipe(recipe.id) },
                    onFavoriteClick = { viewModel.toggleFavorite(recipe.id) },
                )
            }
        }
    }
}
