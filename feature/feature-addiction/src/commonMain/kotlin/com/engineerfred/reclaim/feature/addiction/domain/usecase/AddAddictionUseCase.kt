package com.engineerfred.reclaim.feature.addiction.domain.usecase

import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.domain.model.AddictionType
import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

class AddAddictionUseCase(
    private val authRepository: AuthRepository,
    private val addictionRepository: AddictionRepository
) {
    suspend operator fun invoke(category: AddictionCategory, whyIQuitNote: String?): ReclaimResult<Unit> {
        val user = authRepository.getCurrentUser()
            ?: return ReclaimResult.Failure(IllegalStateException("User not authenticated."))

        val addiction = Addiction(
            id = generateId(),
            userId = user.id,
            category = category,
            type = category.toAddictionType(),
            displayName = formatDisplayName(category),
            startDate = System.currentTimeMillis(),
            isActive = true,
            whyIQuitNote = whyIQuitNote?.trim()?.takeIf { it.isNotEmpty() }
        )

        return addictionRepository.addAddiction(addiction)
    }

    private fun AddictionCategory.toAddictionType(): AddictionType = when (this) {
        AddictionCategory.PORNOGRAPHY, AddictionCategory.GAMBLING, AddictionCategory.BINGE_EATING,
        AddictionCategory.GAMING, AddictionCategory.SHOPPING, AddictionCategory.SELF_HARM -> AddictionType.BEHAVIORAL

        AddictionCategory.ALCOHOL, AddictionCategory.COCAINE, AddictionCategory.MARIJUANA,
        AddictionCategory.NICOTINE, AddictionCategory.PRESCRIPTION_DRUGS -> AddictionType.SUBSTANCE

        AddictionCategory.SOCIAL_MEDIA, AddictionCategory.PHONE, AddictionCategory.STREAMING -> AddictionType.DIGITAL
    }

    private fun formatDisplayName(category: AddictionCategory): String {
        return category.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }
    }

    private fun generateId(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val randomString = (1..10).map { allowedChars.random() }.joinToString("")
        return "${System.currentTimeMillis()}_$randomString"
    }
}