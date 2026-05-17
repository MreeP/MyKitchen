package pl.aplikacjemobilne.mykitchen.ui.screens.home

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import pl.aplikacjemobilne.mykitchen.R
import pl.aplikacjemobilne.mykitchen.ui.components.NamedSection
import pl.aplikacjemobilne.mykitchen.ui.components.RecipeCard
import androidx.core.net.toUri

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToRecipe: (Long) -> Unit,
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

                if (uiState.categories.isNotEmpty()) {
                    val shuffledCategories = remember(uiState.categories) {
                        uiState.categories.shuffled()
                    }

                    var currentIndex by remember { mutableIntStateOf(0) }

                    val currentCategory = shuffledCategories[currentIndex]

                    LaunchedEffect(shuffledCategories.size) {
                        while (true) {
                            delay(3000)
                            currentIndex = (currentIndex + 1) % shuffledCategories.size
                        }
                    }

                    Row {
                        Text(
                            text = "Masz ochotę na ",
                            color = Color(0xFFF5F0EB),
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Serif,
                        )
                        AnimatedContent(
                            targetState = currentCategory,
                            transitionSpec = {
                                (slideInVertically { it } + fadeIn())
                                    .togetherWith(slideOutVertically { -it } + fadeOut())
                            },
                            label = "category-animation"
                        ) { category ->
                            Text(
                                text = "${category.name}?",
                                color = Color(0xFFF5F0EB),
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Serif,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable {
                                    onNavigateToCategory(category.name)
                                }
                            )
                        }
                    }
                }
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
                                text = category.imageUri,
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
                        onClick = { onNavigateToRecipe(recipe.id) },
                    )
                }
            }
        }

        NamedSection(title = "Ciekawostki", textModifier = Modifier.padding(start = 20.dp)) {
            val context = LocalContext.current
            var isPlaying by remember { mutableStateOf(false) }
            val videoUri = remember { "android.resource://${context.packageName}/${R.raw.bitasmietana}".toUri() }

            Surface(
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setVideoURI(videoUri)
                            setOnPreparedListener { mp ->
                                mp.isLooping = true
                            }
                            setOnClickListener {
                                if (isPlaying) {
                                    pause()
                                } else {
                                    start()
                                }

                                isPlaying = !isPlaying
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
