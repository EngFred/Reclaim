package com.engineerfred.reclaim.feature.onboarding.domain.usecase

import com.engineerfred.reclaim.core.domain.model.Addiction
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.domain.model.AddictionType
import com.engineerfred.reclaim.core.domain.repository.AddictionRepository
import com.engineerfred.reclaim.core.domain.repository.AuthRepository
import com.engineerfred.reclaim.core.domain.util.ReclaimResult

/**
 * Executes the final step of onboarding.
 * * Takes the aggregated UI state (selected categories + notes), maps them to
 * fresh [Addiction] domain models, and saves them via [AddictionRepository].
 */
class CompleteOnboardingUseCase(
    private val authRepository: AuthRepository,
    private val addictionRepository: AddictionRepository
) {
    suspend operator fun invoke(
        selections: Map<AddictionCategory, String>
    ): ReclaimResult<Unit> {
        val user = authRepository.getCurrentUser()
            ?: return ReclaimResult.Failure(IllegalStateException("User not authenticated."))

        val now = System.currentTimeMillis()

        selections.forEach { (category, note) ->
            val addiction = Addiction(
                id = generateId(), // Uses timestamp + random string for KMP compatibility
                userId = user.id,
                category = category,
                type = category.toAddictionType(),
                displayName = formatDisplayName(category),
                startDate = now,
                isActive = true,
                whyIQuitNote = note.trim().takeIf { it.isNotEmpty() }
            )

            val result = addictionRepository.addAddiction(addiction)
            if (result is ReclaimResult.Failure) {
                return result // Fail fast if any write fails
            }
        }

        return ReclaimResult.Success(Unit)
    }

    // Maps the specific category back to its parent type (Rule from Section 4/10)
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