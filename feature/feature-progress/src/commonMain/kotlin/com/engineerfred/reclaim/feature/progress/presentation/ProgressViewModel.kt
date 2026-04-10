package com.engineerfred.reclaim.feature.progress.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.feature.progress.domain.usecase.GetCalendarDataUseCase
import com.engineerfred.reclaim.feature.progress.domain.usecase.GetMilestoneMessageUseCase
import com.engineerfred.reclaim.feature.progress.domain.usecase.GetStreakStatsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val addictionRepository: AddictionRepository,
    private val getStreakStatsUseCase: GetStreakStatsUseCase,
    private val getCalendarDataUseCase: GetCalendarDataUseCase,
    private val getMilestoneMessageUseCase: GetMilestoneMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProgressEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var isLoaded = false

    fun loadProgress(addictionId: String) {
        if (isLoaded) return
        isLoaded = true

        val addictionFlow = addictionRepository.observeAddiction(addictionId)
        val streakFlow = getStreakStatsUseCase(addictionId)
        val calendarFlow = getCalendarDataUseCase(addictionId, 90) // Load last 90 days

        combine(addictionFlow, streakFlow, calendarFlow) { addiction, streak, calendar ->
            ProgressUiState(
                addiction = addiction,
                streak = streak,
                calendarDays = calendar,
                currentMilestone = getMilestoneMessageUseCase(streak),
                isLoading = false
            )
        }.onEach { state ->
            _uiState.update { state }
        }.launchIn(viewModelScope)
    }

    fun onBackClicked() {
        viewModelScope.launch { _events.send(ProgressEvent.NavigateBack) }
    }
}