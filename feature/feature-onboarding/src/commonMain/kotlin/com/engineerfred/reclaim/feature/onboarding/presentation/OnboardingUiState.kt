package com.engineerfred.reclaim.feature.onboarding.presentation

import com.engineerfred.reclaim.core.domain.model.AddictionCategory

enum class OnboardingStep {
    WELCOME,
    SELECTION,
    WHY_I_QUIT,
    PERMISSIONS
}

/**
 * Single source of truth for the entire onboarding flow.
 */
data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.WELCOME,
    val selectedCategories: Set<AddictionCategory> = emptySet(),
    val whyIQuitNotes: Map<AddictionCategory, String> = emptyMap(),
    val currentNoteCategoryIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val currentCategoryForNote: AddictionCategory?
        get() = selectedCategories.toList().getOrNull(currentNoteCategoryIndex)

    val canProceedFromSelection: Boolean
        get() = selectedCategories.isNotEmpty()
}