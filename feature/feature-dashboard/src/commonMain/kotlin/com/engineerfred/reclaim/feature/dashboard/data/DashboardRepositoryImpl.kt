package com.engineerfred.reclaim.feature.dashboard.data

import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DashboardRepositoryImpl(
    private val authRepository: AuthRepository
) : DashboardRepository {

    override fun observeGreetingName(): Flow<String> {
        return authRepository.observeCurrentUser().map { user ->
            if (user == null || user.isAnonymous || user.email.isNullOrBlank()) {
                return@map "Warrior" // Fallback for anonymous or incomplete profiles
            }
            // Simple extraction: "alice@example.com" -> "Alice"
            user.email!!.substringBefore("@").replaceFirstChar { it.uppercase() }
        }
    }
}