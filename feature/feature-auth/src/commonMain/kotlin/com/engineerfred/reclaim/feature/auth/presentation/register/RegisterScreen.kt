package com.engineerfred.reclaim.feature.auth.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.engineerfred.reclaim.core.ui.components.LoadingOverlay
import com.engineerfred.reclaim.core.ui.components.ReclaimPrimaryButton
import com.engineerfred.reclaim.core.ui.components.ReclaimTextField
import com.engineerfred.reclaim.core.ui.components.ReclaimTextButton
import com.engineerfred.reclaim.core.ui.navigation.ReclaimRoutes
import com.engineerfred.reclaim.core.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onNavigate: (route: String, clearBackStack: Boolean) -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                RegisterEvent.NavigateToOnboarding -> onNavigate(ReclaimRoutes.WELCOME, true)
                RegisterEvent.NavigateToLogin      -> onNavigate(ReclaimRoutes.LOGIN, false)
                is RegisterEvent.ShowToast         -> { /* wire to SnackbarHost */ }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.screen),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text  = "Start your journey.",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(Spacing.sm))
            Text(
                text      = "Create a free account. No ads. No judgment. Ever.",
                style     = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.xxxl))

            // ── Email ──────────────────────────────────────────────────────
            ReclaimTextField(
                value           = uiState.email,
                onValueChange   = viewModel::onEmailChanged,
                label           = "Email",
                placeholder     = "you@example.com",
                isError         = uiState.emailError != null,
                errorMessage    = uiState.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction    = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            // ── Password ───────────────────────────────────────────────────
            ReclaimTextField(
                value           = uiState.password,
                onValueChange   = viewModel::onPasswordChanged,
                label           = "Password",
                isError         = uiState.passwordError != null,
                errorMessage    = uiState.passwordError,
                visualTransformation = if (uiState.isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                trailingIcon = {
                    IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            // ── Confirm password ───────────────────────────────────────────
            ReclaimTextField(
                value           = uiState.confirmPassword,
                onValueChange   = viewModel::onConfirmPasswordChanged,
                label           = "Confirm Password",
                isError         = uiState.confirmPasswordError != null,
                errorMessage    = uiState.confirmPasswordError,
                visualTransformation = if (uiState.isConfirmPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.onRegisterClicked()
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = viewModel::onToggleConfirmPasswordVisibility) {
                        Icon(
                            imageVector = if (uiState.isConfirmPasswordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )

            if (uiState.generalError != null) {
                Spacer(modifier = Modifier.height(Spacing.sm))
                Text(
                    text      = uiState.generalError!!,
                    style     = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.error
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(Spacing.xxl))

            ReclaimPrimaryButton(
                text      = "Create Account",
                onClick   = viewModel::onRegisterClicked,
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            ReclaimTextButton(
                text    = "Already have an account? Sign in",
                onClick = viewModel::onBackToLoginClicked
            )
        }

        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}