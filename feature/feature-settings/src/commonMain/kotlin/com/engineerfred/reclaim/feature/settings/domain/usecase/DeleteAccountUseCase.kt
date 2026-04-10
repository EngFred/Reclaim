package com.engineerfred.reclaim.feature.settings.domain.usecase

import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult
import com.engineerfred.reclaim.core.notifications.NotificationScheduler

class DeleteAccountUseCase(
    private val authRepository: AuthRepository,
    private val notificationScheduler: NotificationScheduler
) {
    suspend operator fun invoke(): ReclaimResult<Unit> {
        // 1. Cancel all scheduled local notifications immediately
        notificationScheduler.cancelAll()

        // 2. Trigger remote and local database wipe via AuthRepository
        return authRepository.deleteAccount()
    }
}