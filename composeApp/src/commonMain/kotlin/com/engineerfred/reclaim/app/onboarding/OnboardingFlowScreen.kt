package com.engineerfred.reclaim.app.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.notifications.NotificationScheduler
import com.engineerfred.reclaim.feature.onboarding.presentation.OnboardingStep
import com.engineerfred.reclaim.feature.onboarding.presentation.OnboardingViewModel
import com.engineerfred.reclaim.feature.onboarding.presentation.permission.NotificationPermissionScreen
import com.engineerfred.reclaim.feature.onboarding.presentation.selection.AddictionSelectionScreen
import com.engineerfred.reclaim.feature.onboarding.presentation.welcome.WelcomeScreen
import com.engineerfred.reclaim.feature.onboarding.presentation.whyiquit.WhyIQuitScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingFlowScreen(
    onNavigateToDashboard: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
    notificationScheduler: NotificationScheduler = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.currentStep) {
        OnboardingStep.WELCOME -> {
            WelcomeScreen(onContinue = viewModel::onStartClicked)
        }
        OnboardingStep.SELECTION -> {
            AddictionSelectionScreen(
                selectedCategories = uiState.selectedCategories,
                onToggleCategory = viewModel::toggleCategory,
                canProceed = uiState.canProceedFromSelection,
                onContinue = viewModel::onSelectionContinueClicked
            )
        }
        OnboardingStep.WHY_I_QUIT -> {
            val currentCategory = uiState.currentCategoryForNote
            if (currentCategory != null) {
                WhyIQuitScreen(
                    category = currentCategory,
                    noteText = uiState.whyIQuitNotes[currentCategory] ?: "",
                    onNoteChange = viewModel::updateCurrentNote,
                    onContinue = viewModel::onNoteContinueClicked,
                    onSkip = viewModel::onNoteSkipClicked
                )
            }
        }
        OnboardingStep.PERMISSIONS -> {
            NotificationPermissionScreen(
                notificationScheduler = notificationScheduler,
                onComplete = {
                    viewModel.onPermissionsHandled()
                    onNavigateToDashboard()
                }
            )
        }
    }
}