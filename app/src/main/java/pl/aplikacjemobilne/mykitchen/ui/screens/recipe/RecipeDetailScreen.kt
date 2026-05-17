package pl.aplikacjemobilne.mykitchen.ui.screens.recipe

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import pl.aplikacjemobilne.mykitchen.data.local.entity.IngredientEntity

@Composable
fun RecipeDetailScreen(
    recipeId: Long,
    onNavigateBack: () -> Unit,
    onStartCooking: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(recipeId) {
        viewModel.setRecipeId(recipeId)
    }

    val recipe = uiState.recipe

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F0EB))
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D5016)),
        ) {
            if (recipe != null) {
                AsyncImage(
                    model = recipe.imageUri,
                    contentDescription = recipe.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                )
            } else {
                Spacer(Modifier.height(300.dp))
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(16.dp),
            ) {
                Surface(
                    onClick = onNavigateBack,
                    shape = CircleShape,
                    color = Color(0x55000000),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Wróć",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp),
                    )
                }

                Surface(
                    onClick = { viewModel.toggleFavorite() },
                    shape = CircleShape,
                    color = Color(0x55000000),
                ) {
                    Icon(
                        imageVector = if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (uiState.isFavorite) "Usuń z ulubionych" else "Dodaj do ulubionych",
                        tint = if (uiState.isFavorite) Color(0xFFE53935) else Color.White,
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }

        if (recipe != null) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF1A1A1A),
                )
                Text(
                    text = "👩‍🍳 przez ${recipe.authorName}",
                    fontSize = 14.sp,
                    color = Color(0xFF8A8A8A),
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                ) {
                    StatItem(value = recipe.time.toString(), label = "MINUT")
                    StatItem(value = uiState.ingredients.size.toString(), label = "SKŁADNIKI")
                    StatItem(value = "⭐ ${recipe.rating}", label = "OCENA")
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Składniki",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            Spacer(Modifier.height(8.dp))

            uiState.ingredients.forEach { ingredient ->
                IngredientRow(ingredient)
                HorizontalDivider(
                    color = Color(0xFFEEEEEE),
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onStartCooking(recipeId) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD4540A),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(56.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(
                    text = "Zacznij gotować",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF8A8A8A),
            letterSpacing = 1.sp,
        )
    }
}

@Composable
private fun IngredientRow(ingredient: IngredientEntity) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "●",
                fontSize = 8.sp,
                color = Color(0xFFD4540A),
            )
            Text(
                text = ingredient.name,
                fontSize = 15.sp,
                color = Color(0xFF1A1A1A),
            )
        }
        val amountText = if (ingredient.amount.isNotEmpty()) {
            "${ingredient.amount}${ingredient.unit}"
        } else {
            ingredient.unit
        }
        Text(
            text = amountText,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFD4540A),
        )
    }
}
