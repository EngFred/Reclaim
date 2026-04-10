package com.engineerfred.reclaim.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.feature.dashboard.domain.usecase.GetAllActiveAddictionsUseCase
import com.engineerfred.reclaim.feature.dashboard.domain.usecase.GetStreakForAddictionUseCase
import com.engineerfred.reclaim.feature.dashboard.domain.usecase.GetTodayGreetingUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel(
    private val getTodayGreetingUseCase: GetTodayGreetingUseCase,
    private val getAllActiveAddictionsUseCase: GetAllActiveAddictionsUseCase,
    private val getStreakForAddictionUseCase: GetStreakForAddictionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _events = Channel<DashboardEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        observeGreeting()
        observeAddictionsAndStreaks()
    }

    private fun observeGreeting() {
        getTodayGreetingUseCase().onEach { greetingText ->
            _uiState.update { it.copy(greeting = greetingText) }
        }.launchIn(viewModelScope)
    }

    private fun observeAddictionsAndStreaks() {
        getAllActiveAddictionsUseCase().flatMapLatest { addictions ->
            if (addictions.isEmpty()) {
                _uiState.update { it.copy(isLoading = false, activeAddictions = emptyList()) }
                flowOf(emptyList())
            } else {
                // Combine every addiction with its live streak data
                val streakFlows = addictions.map { addiction ->
                    getStreakForAddictionUseCase(addiction.id).map { streak ->
                        ActiveAddictionItem(addiction, streak)
                    }
                }
                combine(streakFlows) { itemsArray ->
                    itemsArray.toList().sortedByDescending { it.addiction.startDate }
                }
            }
        }.onEach { combinedList ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    activeAddictions = combinedList
                )
            }
        }.launchIn(viewModelScope)
    }

    // ── User Actions ──────────────────────────────────────────────────────────

    fun onCheckInClicked(addictionId: String) {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToCheckIn(addictionId))
        }
    }

    fun onAddictionCardClicked(addictionId: String) {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToAddictionDetail(addictionId))
        }
    }

    fun onAddAddictionClicked() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToAddAddiction)
        }
    }
}