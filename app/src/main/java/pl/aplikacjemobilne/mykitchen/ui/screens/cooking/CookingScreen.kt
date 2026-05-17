package pl.aplikacjemobilne.mykitchen.ui.screens.cooking

import android.media.RingtoneManager
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

private val DarkBg = Color(0xFF1A1200)
private val DarkCard = Color(0xFF2A2218)
private val OrangeAccent = Color(0xFFD4540A)
private val TimerCardBg = Color(0xFF3A2A18)

@Composable
fun CookingScreen(
    recipeId: Long,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CookingViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showLeaveDialog by remember { mutableStateOf(false) }

    BackHandler { showLeaveDialog = true }

    LaunchedEffect(recipeId) {
        viewModel.setRecipeId(recipeId)
    }

    LaunchedEffect(Unit) {
        viewModel.finishEvent.collect { onFinish() }
    }

    LaunchedEffect(Unit) {
        viewModel.timerDoneEvent.collect {
            try {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                RingtoneManager.getRingtone(context, uri)?.play()
            } catch (_: Exception) { }
        }
    }

    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = { Text("Przerwać gotowanie?") },
            text = { Text("Postęp nie zostanie zapisany.") },
            confirmButton = {
                TextButton(onClick = {
                    showLeaveDialog = false
                    onFinish()
                }) {
                    Text("Wyjdź", color = OrangeAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog = false }) {
                    Text("Zostań")
                }
            },
        )
    }

    val currentStep = uiState.steps.getOrNull(uiState.currentStepIndex)
    val totalSteps = uiState.steps.size
    val isFirstStep = uiState.currentStepIndex == 0
    val isLastStep = uiState.currentStepIndex == totalSteps - 1

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Surface(
                onClick = { showLeaveDialog = true },
                shape = CircleShape,
                color = Color(0xFF2A2A2A),
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Wróć",
                        tint = Color(0xFFF5F0EB),
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            Text(
                text = uiState.recipeName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                color = Color(0xFFF5F0EB),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            )

            if (totalSteps > 0) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = OrangeAccent,
                ) {
                    Text(
                        text = "Krok ${uiState.currentStepIndex + 1} z $totalSteps",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (totalSteps > 0) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                for (i in 0 until totalSteps) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (i <= uiState.currentStepIndex) OrangeAccent
                                else Color(0xFF4A4A4A)
                            ),
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        if (currentStep != null) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = DarkCard,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                ) {
                    if (currentStep.imageUri != null) {
                        AsyncImage(
                            model = currentStep.imageUri,
                            contentDescription = "Krok ${currentStep.stepNumber}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp)),
                        )
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                        ) {
                            Text(text = "🍳", fontSize = 48.sp)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "KROK ${currentStep.stepNumber}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeAccent,
                        letterSpacing = 2.sp,
                    )

                    Spacer(Modifier.height(8.dp))

                    ExpandableStepText(
                        description = currentStep.description,
                        ingredients = uiState.allIngredients,
                    )

                    if (uiState.currentStepIngredients.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "SKŁADNIKI DO TEGO KROKU",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9A9A9A),
                            letterSpacing = 1.sp,
                        )

                        Spacer(Modifier.height(6.dp))

                        uiState.currentStepIngredients.forEach { ingredient ->
                            Text(
                                text = "• ${ingredient.unit.format(ingredient.amount)} ${ingredient.name}",
                                fontSize = 14.sp,
                                color = Color(0xFFCCCCCC),
                            )
                        }
                    }
                }
            }

            if (currentStep.timerSeconds != null && currentStep.timerSeconds > 0) {
                Spacer(Modifier.height(16.dp))

                TimerCard(
                    seconds = uiState.timerSeconds,
                    isRunning = uiState.timerRunning,
                    onStart = viewModel::startTimer,
                    onPause = viewModel::pauseTimer,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { viewModel.previousStep() },
                enabled = !isFirstStep,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeAccent,
                    disabledContainerColor = Color(0xFF2A2A2A),
                    disabledContentColor = Color(0xFF6A6A6A),
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
            ) {
                Text(
                    text = "← Poprzedni",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Button(
                onClick = {
                    if (isLastStep) viewModel.finish() else viewModel.nextStep()
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
            ) {
                Text(
                    text = if (isLastStep) "Zakończ ✓" else "Następny →",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun TimerCard(
    seconds: Int,
    isRunning: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
) {
    val minutes = seconds / 60
    val secs = seconds % 60
    val timeText = "%d:%02d".format(minutes, secs)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = TimerCardBg,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = "⏱️", fontSize = 28.sp)
                Column {
                    Text(
                        text = timeText,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeAccent,
                    )
                    Text(
                        text = "minuty",
                        fontSize = 13.sp,
                        color = Color(0xFF9A9A9A),
                    )
                }
            }

            Surface(
                onClick = { if (isRunning) onPause() else onStart() },
                shape = CircleShape,
                color = OrangeAccent,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isRunning) "Pauza" else "Start",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
        }
    }
}
