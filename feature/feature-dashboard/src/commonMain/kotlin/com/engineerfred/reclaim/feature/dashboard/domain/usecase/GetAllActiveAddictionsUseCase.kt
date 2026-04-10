package com.engineerfred.reclaim.feature.dashboard.domain.usecase

import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class GetAllActiveAddictionsUseCase(
    private val authRepository: AuthRepository,
    private val addictionRepository: AddictionRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Addiction>> {
        return authRepository.observeCurrentUser().flatMapLatest { user ->
            if (user == null) {
                flowOf(emptyList())
            } else {
                addictionRepository.observeActiveAddictions(user.id)
            }
        }
    }
}