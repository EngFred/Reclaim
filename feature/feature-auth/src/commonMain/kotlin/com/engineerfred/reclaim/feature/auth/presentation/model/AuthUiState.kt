package com.engineerfred.reclaim.feature.auth.presentation.model

import com.engineerfred.reclaim.core.domain.model.User

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Success(val user: User) : AuthUiState
    data class Error(val message: String) : AuthUiState
}