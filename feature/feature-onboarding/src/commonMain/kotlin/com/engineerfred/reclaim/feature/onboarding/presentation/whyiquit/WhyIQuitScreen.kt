package com.engineerfred.reclaim.feature.onboarding.presentation.whyiquit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.engineerfred.reclaim.core.domain.model.AddictionCategory
import com.engineerfred.reclaim.core.ui.components.ReclaimNoteField
import com.engineerfred.reclaim.core.ui.components.ReclaimOutlinedButton
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.theme.Spacing

@Composable
fun WhyIQuitScreen(
    category: AddictionCategory,
    noteText: String,
    onNoteChange: (String) -> Unit,
    onContinue: () -> Unit,
    onSkip: () -> Unit
) {
    val displayName = category.name.lowercase().replace("_", " ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.screen)
    ) {
        Text(
            text = "Why are you quitting $displayName?",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        Text(
            text = "Write a short note to your future self. We'll show this to you when you hit the SOS button during strong urges.",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Spacer(modifier = Modifier.height(Spacing.xxl))

        ReclaimNoteField(
            value = noteText,
            onValueChange = onNoteChange,
            label = "Your 'Why'",
            placeholder = "I am quitting because...",
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            ReclaimOutlinedButton(
                text = "Skip",
                onClick = onSkip,
                modifier = Modifier.weight(1f)
            )
            ReclaimPrimaryButton(
                text = "Save",
                onClick = onContinue,
                modifier = Modifier.weight(1f),
                enabled = noteText.isNotBlank()
            )
        }
    }
}