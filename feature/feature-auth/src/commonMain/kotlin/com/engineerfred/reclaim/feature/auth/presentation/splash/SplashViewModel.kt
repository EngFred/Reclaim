package com.engineerfred.reclaim.feature.auth.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.feature.auth.domain.usecase.GetCurrentUserUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * SplashScreen has exactly one job: determine where to navigate.
 *
 * It observes the auth state and emits a [SplashDestination] that
 * the Composable uses to navigate — once, immediately, without delay.
 *
 * No loading states, no retry logic. If Firebase is unreachable,
 * the local SQLDelight-backed auth state is used as the source of truth.
 */
class SplashViewModel(
    getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    val destination: StateFlow<SplashDestination> =
        getCurrentUser()
            .map { user ->
                if (user != null) SplashDestination.Dashboard
                else SplashDestination.Login
            }
            .stateIn(
                scope         = viewModelScope,
                started       = SharingStarted.WhileSubscribed(5_000),
                initialValue  = SplashDestination.Undecided
            )
}

/**
 * The three possible outcomes from the splash screen.
 * [Undecided] is only the transient initial value while the first
 * auth state emission is in flight.
 */
sealed interface SplashDestination {
    data object Undecided  : SplashDestination
    data object Login      : SplashDestination
    data object Dashboard  : SplashDestination
}