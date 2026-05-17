package pl.aplikacjemobilne.mykitchen.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import pl.aplikacjemobilne.mykitchen.ui.components.NamedSection
import pl.aplikacjemobilne.mykitchen.ui.components.RecipeCard
import pl.aplikacjemobilne.mykitchen.ui.components.RecipeListCard

@Composable
fun SearchScreen(
    onNavigateToRecipe: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F0EB))
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Text(
            text = "Szukaj",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFF1A1A1A),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        )

        OutlinedTextField(
            value = uiState.query,
            onValueChange = viewModel::onQueryChange,
            placeholder = { Text("Szukaj przepisów...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            },
            trailingIcon = {
                if (uiState.query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Wyczyść",
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFFD4540A),
                unfocusedBorderColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        if (uiState.query.isBlank()) {
            RecommendedContent(uiState, onNavigateToRecipe, viewModel::toggleFavorite)
        } else {
            SearchResultsContent(uiState, onNavigateToRecipe, viewModel::toggleFavorite)
        }
    }
}

@Composable
private fun RecommendedContent(
    uiState: SearchUiState,
    onNavigateToRecipe: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit,
) {
    NamedSection(title = "Polecane dzisiaj", textModifier = Modifier.padding(start = 20.dp).padding(top = 20.dp)) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        ) {
            items(uiState.recipes, key = { it.id }) { recipe ->
                RecipeListCard(
                    recipe = recipe,
                    isFavorite = uiState.favoriteIds.contains(recipe.id),
                    onClick = { onNavigateToRecipe(recipe.id) },
                    onFavoriteClick = { onToggleFavorite(recipe.id) },
                )
            }
        }
    }
}

@Composable
private fun SearchResultsContent(
    uiState: SearchUiState,
    onNavigateToRecipe: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit,
) {
    if (uiState.recipes.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
        ) {
            Text(
                text = "🔍",
                fontSize = 48.sp,
            )
            Text(
                text = "Nie znaleziono przepisów",
                fontSize = 16.sp,
                color = Color(0xFF8A8A8A),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        ) {
            items(uiState.recipes, key = { it.id }) { recipe ->
                RecipeListCard(
                    recipe = recipe,
                    isFavorite = uiState.favoriteIds.contains(recipe.id),
                    onClick = { onNavigateToRecipe(recipe.id) },
                    onFavoriteClick = { onToggleFavorite(recipe.id) },
                )
            }
        }
    }
}
