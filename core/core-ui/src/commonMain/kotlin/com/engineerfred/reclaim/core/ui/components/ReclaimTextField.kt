package com.engineerfred.reclaim.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.engineerfred.reclaim.core.ui.theme.CornerRadius

/**
 * Shared text input component used across auth, onboarding, and settings screens.
 *
 * Wraps OutlinedTextField with consistent shape and colour tokens.
 * Supports password masking, error states, and trailing icons.
 */
@Composable
fun ReclaimTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value             = value,
        onValueChange     = onValueChange,
        label             = { Text(label) },
        placeholder       = if (placeholder.isNotEmpty()) ({ Text(placeholder) }) else null,
        modifier          = modifier.fillMaxWidth(),
        enabled           = enabled,
        isError           = isError,
        singleLine        = singleLine,
        maxLines          = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions   = keyboardOptions,
        keyboardActions   = keyboardActions,
        leadingIcon       = leadingIcon,
        trailingIcon      = trailingIcon,
        supportingText    = if (isError && errorMessage != null) {
            { Text(text = errorMessage, color = MaterialTheme.colorScheme.error) }
        } else null,
        shape             = RoundedCornerShape(CornerRadius.md),
        colors            = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor     = MaterialTheme.colorScheme.error,
            focusedLabelColor    = MaterialTheme.colorScheme.primary,
            cursorColor          = MaterialTheme.colorScheme.primary
        )
    )
}

/**
 * Multi-line note input — used for "Why I Quit" notes and trigger notes.
 */
@Composable
fun ReclaimNoteField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label) },
        placeholder   = if (placeholder.isNotEmpty()) ({ Text(placeholder) }) else null,
        modifier      = modifier.fillMaxWidth(),
        enabled       = enabled,
        singleLine    = false,
        maxLines      = 6,
        minLines      = 3,
        shape         = RoundedCornerShape(CornerRadius.md),
        colors        = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor    = MaterialTheme.colorScheme.primary,
            cursorColor          = MaterialTheme.colorScheme.primary
        )
    )
}