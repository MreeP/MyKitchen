package pl.aplikacjemobilne.mykitchen.ui.screens.cooking

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.aplikacjemobilne.mykitchen.MyKitchenApp
import pl.aplikacjemobilne.mykitchen.data.local.entity.StepEntity

data class CookingUiState(
    val recipeName: String = "",
    val steps: List<StepEntity> = emptyList(),
    val currentStepIndex: Int = 0,
    val timerSeconds: Int = 0,
    val timerRunning: Boolean = false,
    val timerInitialSeconds: Int = 0,
)

class CookingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MyKitchenApp).repository

    private val _uiState = MutableStateFlow(CookingUiState())
    val uiState: StateFlow<CookingUiState> = _uiState.asStateFlow()

    private val _timerDoneEvent = MutableSharedFlow<Unit>()
    val timerDoneEvent: SharedFlow<Unit> = _timerDoneEvent.asSharedFlow()

    private val _finishEvent = MutableSharedFlow<Unit>()
    val finishEvent: SharedFlow<Unit> = _finishEvent.asSharedFlow()

    private var timerJob: Job? = null
    private var recipeId: Long = 0L

    fun setRecipeId(id: Long) {
        recipeId = id
        viewModelScope.launch {
            repository.getRecipeWithDetails(id).collect { details ->
                if (details != null) {
                    val steps = details.steps.sortedBy { it.stepNumber }
                    _uiState.value = _uiState.value.copy(
                        recipeName = details.recipe.name,
                        steps = steps,
                    )
                    loadTimerForStep(0, steps)
                }
            }
        }
    }

    private fun loadTimerForStep(index: Int, steps: List<StepEntity> = _uiState.value.steps) {
        timerJob?.cancel()
        val step = steps.getOrNull(index)
        val timerSec = step?.timerSeconds ?: 0
        _uiState.value = _uiState.value.copy(
            currentStepIndex = index,
            timerSeconds = timerSec,
            timerRunning = false,
            timerInitialSeconds = timerSec,
        )
    }

    fun nextStep() {
        val state = _uiState.value
        if (state.currentStepIndex < state.steps.size - 1) {
            loadTimerForStep(state.currentStepIndex + 1)
        }
    }

    fun previousStep() {
        val state = _uiState.value
        if (state.currentStepIndex > 0) {
            loadTimerForStep(state.currentStepIndex - 1)
        }
    }

    fun startTimer() {
        if (_uiState.value.timerSeconds <= 0) return
        _uiState.value = _uiState.value.copy(timerRunning = true)
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timerSeconds > 0 && _uiState.value.timerRunning) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    timerSeconds = _uiState.value.timerSeconds - 1,
                )
            }
            if (_uiState.value.timerSeconds <= 0) {
                _uiState.value = _uiState.value.copy(timerRunning = false)
                _timerDoneEvent.emit(Unit)
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(timerRunning = false)
    }

    fun finish() {
        viewModelScope.launch {
            repository.addCookingHistory(recipeId)
            _finishEvent.emit(Unit)
        }
    }
}
