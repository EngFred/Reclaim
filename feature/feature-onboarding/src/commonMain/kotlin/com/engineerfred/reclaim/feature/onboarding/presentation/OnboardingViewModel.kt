package com.engineerfred.reclaim.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _events = Channel<OnboardingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // ── Navigation Actions ────────────────────────────────────────────────────

    fun onStartClicked() {
        _uiState.update { it.copy(currentStep = OnboardingStep.SELECTION) }
    }

    fun onSelectionContinueClicked() {
        if (_uiState.value.selectedCategories.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    currentStep = OnboardingStep.WHY_I_QUIT,
                    currentNoteCategoryIndex = 0
                )
            }
        }
    }

    fun onNoteContinueClicked() {
        val state = _uiState.value
        if (state.currentNoteCategoryIndex < state.selectedCategories.size - 1) {
            // Move to the next selected addiction's note
            _uiState.update { it.copy(currentNoteCategoryIndex = it.currentNoteCategoryIndex + 1) }
        } else {
            // All notes done, move to permissions
            _uiState.update { it.copy(currentStep = OnboardingStep.PERMISSIONS) }
        }
    }

    fun onNoteSkipClicked() {
        onNoteContinueClicked()
    }

    // ── State Updates ─────────────────────────────────────────────────────────

    fun toggleCategory(category: AddictionCategory) {
        _uiState.update { state ->
            val newSelections = if (state.selectedCategories.contains(category)) {
                state.selectedCategories - category
            } else {
                state.selectedCategories + category
            }
            state.copy(selectedCategories = newSelections)
        }
    }

    fun updateCurrentNote(note: String) {
        _uiState.update { state ->
            val currentCategory = state.currentCategoryForNote ?: return@update state
            val updatedNotes = state.whyIQuitNotes.toMutableMap().apply {
                put(currentCategory, note)
            }
            state.copy(whyIQuitNotes = updatedNotes)
        }
    }

    // ── Finalization ──────────────────────────────────────────────────────────

    fun onPermissionsHandled() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = completeOnboardingUseCase(state.whyIQuitNotes)

            when (result) {
                is ReclaimResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(OnboardingEvent.CompleteAndNavigateToDashboard)
                }
                is ReclaimResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = result.exception.message ?: "Failed to save setup.")
                    }
                    _events.send(OnboardingEvent.ShowToast("Failed to save. Please try again."))
                }
            }
        }
    }
}