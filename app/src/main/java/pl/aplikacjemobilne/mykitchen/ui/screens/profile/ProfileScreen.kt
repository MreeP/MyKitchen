package pl.aplikacjemobilne.mykitchen.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.aplikacjemobilne.mykitchen.ui.components.ProfileRecipeCard

@Composable
fun ProfileScreen(
    onNavigateToRecipe: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F0EB)),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF5D3A1A), Color(0xFF8B6840))
                            )
                        )
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .height(140.dp),
                )
                Spacer(Modifier.height(52.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 20.dp, end = 20.dp, top = 28.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFD4874A),
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(60.dp),
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(text = "👨‍🍳", fontSize = 30.sp)
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(start = 14.dp),
                ) {
                    Text(
                        text = ProfileViewModel.USER_NAME,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = ProfileViewModel.USER_UNIQUE_NAME,
                        fontSize = 13.sp,
                        color = Color(0xFFBBAAAA),
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.BottomCenter),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                ) {
                    StatItem(value = "${uiState.recipes.size}", label = "PRZEPISÓW")
                    StatItem(value = "312", label = "OBSERWUJE")
                    StatItem(value = "1.2k", label = "POLUBIEŃ")
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(horizontal = 20.dp),
        ) {
            ProfileTab.entries.forEach { tab ->
                val isSelected = uiState.selectedTab == tab

                val label = when (tab) {
                    ProfileTab.MY_RECIPES -> "Moje przepisy"
                    ProfileTab.FAVORITES -> "Ulubione"
                    ProfileTab.HISTORY -> "Historia"
                }

                Surface(
                    onClick = { viewModel.selectTab(tab) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) Color.White else Color.Transparent,
                    shadowElevation = if (isSelected) 2.dp else 0.dp,
                ) {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) Color(0xFF1A1A1A) else Color(0xFF8A8A8A),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    )
                }
            }
        }

        if (uiState.recipes.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
            ) {
                Text(text = "📭", fontSize = 48.sp)
                Text(
                    text = when (uiState.selectedTab) {
                        ProfileTab.MY_RECIPES -> "Brak przepisów"
                        ProfileTab.FAVORITES -> "Brak ulubionych"
                        ProfileTab.HISTORY -> "Brak historii"
                    },
                    fontSize = 16.sp,
                    color = Color(0xFF8A8A8A),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            ) {
                items(uiState.recipes, key = { it.id }) { recipe ->
                    ProfileRecipeCard(
                        recipe = recipe,
                        onClick = { onNavigateToRecipe(recipe.id) },
                    )
                }
            }
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
